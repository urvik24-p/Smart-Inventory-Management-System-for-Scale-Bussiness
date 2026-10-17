/* pages/stockout.js — Serial or Quantity mode */

var soSerials = [];

async function renderStockOut(container) {
  soSerials = [];
  var products = [];
  try { products = await ProductAPI.getAll(); }
  catch(err) { container.innerHTML = '<div style="color:var(--danger);padding:20px">' + escHtml(err.message) + '</div>'; return; }

  container.innerHTML =
    '<div style="max-width:640px;">' +
    '<div class="card">' +
      '<div class="card-header"><h6><i class="bi bi-box-arrow-up"></i> Dispatch Stock</h6></div>' +
      '<div style="padding:20px;">' +
        '<form id="so-form">' +

          '<div class="form-group">' +
            '<label class="form-label">Product *</label>' +
            '<select class="form-input" id="so-product" required>' +
              '<option value="">— Select Product —</option>' +
              products.map(function(p) {
                return '<option value="' + p.productId + '">' + escHtml(p.productName) + (p.brand ? ' · ' + escHtml(p.brand) : '') + '</option>';
              }).join('') +
            '</select>' +
          '</div>' +

          '<div class="form-group">' +
            '<label class="form-label">Entry Mode</label>' +
            '<div class="mode-toggle">' +
              '<button type="button" class="mode-btn active red-mode" data-m="serial"><i class="bi bi-upc-scan"></i> Serial Numbers</button>' +
              '<button type="button" class="mode-btn" data-m="qty"><i class="bi bi-123"></i> Quantity Only</button>' +
            '</div>' +
          '</div>' +

          // Serial panel
          '<div id="so-serial-panel">' +
            '<div id="so-avail" class="form-group"></div>' +
            '<div class="form-group">' +
              '<label class="form-label">Selected Serial Numbers</label>' +
              '<div style="display:flex;gap:8px;">' +
                '<input class="form-input" type="text" id="so-sn-input" placeholder="Type serial number..." style="flex:1">' +
                '<button type="button" class="btn-red" id="so-add-btn"><i class="bi bi-plus-lg"></i> Add</button>' +
              '</div>' +
              '<div class="tags-area" id="so-tags"><span style="color:var(--text-muted);font-size:12px">None selected</span></div>' +
            '</div>' +
          '</div>' +

          // Qty panel
          '<div id="so-qty-panel" style="display:none;">' +
            '<div class="form-group">' +
              '<label class="form-label">Quantity to Dispatch *</label>' +
              '<div style="display:flex;align-items:center;gap:12px;">' +
                '<div class="qty-wrap">' +
                  '<button type="button" class="qty-btn" id="so-minus">−</button>' +
                  '<span class="qty-val" id="so-qty-val">1</span>' +
                  '<button type="button" class="qty-btn" id="so-plus">+</button>' +
                '</div>' +
                '<input class="form-input" type="number" id="so-qty" value="1" min="1" style="width:90px;">' +
              '</div>' +
              '<p style="font-size:11px;color:var(--text-muted);margin-top:6px;"><i class="bi bi-info-circle"></i> Will automatically select the oldest available units.</p>' +
            '</div>' +
          '</div>' +

          '<div class="form-group">' +
            '<label class="form-label">Reference Note <span class="opt">(optional)</span></label>' +
            '<input class="form-input" type="text" id="so-note" placeholder="e.g. Customer: ABC Labs, Invoice #101">' +
          '</div>' +

          '<button type="submit" class="btn-red" style="width:100%;justify-content:center;padding:10px;">' +
            '<i class="bi bi-box-arrow-up"></i> Process Dispatch' +
          '</button>' +

        '</form>' +
      '</div>' +
    '</div>' +
    '</div>';

  var mode = 'serial', soQty = 1;

  document.querySelectorAll('.mode-btn').forEach(function(btn) {
    btn.addEventListener('click', function() {
      mode = btn.dataset.m;
      document.querySelectorAll('.mode-btn').forEach(function(b) { b.classList.remove('active','red-mode'); });
      btn.classList.add('active','red-mode');
      document.getElementById('so-serial-panel').style.display = mode==='serial' ? '' : 'none';
      document.getElementById('so-qty-panel').style.display    = mode==='qty'    ? '' : 'none';
    });
  });

  document.getElementById('so-plus').addEventListener('click', function() { soQty=Math.min(9999,soQty+1); syncQ(); });
  document.getElementById('so-minus').addEventListener('click', function() { soQty=Math.max(1,soQty-1);   syncQ(); });
  document.getElementById('so-qty').addEventListener('input', function() { soQty=Math.max(1,parseInt(this.value)||1); syncQ(); });
  function syncQ() { document.getElementById('so-qty').value=soQty; document.getElementById('so-qty-val').textContent=soQty; }

  // Load available serials on product change
  document.getElementById('so-product').addEventListener('change', async function() {
    var pid = this.value;
    var div = document.getElementById('so-avail');
    if (!pid) { div.innerHTML=''; return; }
    div.innerHTML = '<p style="font-size:12px;color:var(--text-sec)">Loading available units...</p>';
    try {
      var avail = await SerialAPI.getByProductAndStatus(pid, 'available');
      if (avail.length === 0) {
        div.innerHTML = '<p style="font-size:12px;color:var(--warning);background:var(--warning-bg);padding:8px 12px;border-radius:7px;"><i class="bi bi-exclamation-triangle"></i> No available units for this product.</p>';
      } else {
        div.innerHTML =
          '<div class="form-group">' +
            '<label class="form-label">Available Units — click to select</label>' +
            '<div style="display:flex;flex-wrap:wrap;gap:6px;">' +
            avail.map(function(s) {
              return '<span class="avail-badge" data-sn="' + escHtml(s.serialNumber) + '"><i class="bi bi-upc"></i> ' + escHtml(s.serialNumber) + '</span>';
            }).join('') +
            '</div>' +
          '</div>';
        div.querySelectorAll('.avail-badge').forEach(function(b) {
          b.addEventListener('click', function() {
            var val = b.dataset.sn;
            if (!soSerials.includes(val)) { soSerials.push(val); b.classList.add('picked'); renderSoTags(); }
          });
        });
      }
    } catch(err) { div.innerHTML = '<p style="font-size:12px;color:var(--danger)">Could not load units.</p>'; }
  });

  document.getElementById('so-add-btn').addEventListener('click', addSoSerial);
  document.getElementById('so-sn-input').addEventListener('keydown', function(e) { if(e.key==='Enter'){e.preventDefault();addSoSerial();} });

  document.getElementById('so-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    var pid  = document.getElementById('so-product').value;
    var note = document.getElementById('so-note').value.trim();
    if (!pid) { showToast('Select a product', 'error'); return; }
    try {
      if (mode === 'serial') {
        if (soSerials.length === 0) { showToast('Select at least one serial number', 'error'); return; }
        await StockOutAPI.process(parseInt(pid), soSerials, note);
        showToast(soSerials.length + ' unit(s) dispatched!', 'success');
      } else {
        soQty = parseInt(document.getElementById('so-qty').value)||1;
        var avail = await SerialAPI.getByProductAndStatus(pid, 'available');
        if (avail.length === 0) { showToast('No available stock for this product', 'error'); return; }
        var pick = avail.slice(0, soQty).map(function(s) { return s.serialNumber; });
        await StockOutAPI.process(parseInt(pid), pick, note||'Bulk dispatch');
        showToast(pick.length + ' unit(s) dispatched!', 'success');
      }
      soSerials = [];
      renderStockOut(container);
    } catch(err) { showToast(err.message, 'error'); }
  });
}

function addSoSerial() {
  var inp = document.getElementById('so-sn-input');
  var val = inp.value.trim();
  if (!val) return;
  if (soSerials.includes(val)) { showToast('Already selected', 'error'); return; }
  soSerials.push(val); inp.value=''; inp.focus(); renderSoTags();
}

function renderSoTags() {
  var div = document.getElementById('so-tags');
  if (soSerials.length===0) { div.innerHTML='<span style="color:var(--text-muted);font-size:12px">None selected</span>'; return; }
  div.innerHTML = soSerials.map(function(sn,i) {
    return '<span class="serial-tag" style="background:var(--danger-bg);color:var(--danger);border-color:var(--danger-bdr);">' +
      escHtml(sn) + '<button data-i="' + i + '">✕</button></span>';
  }).join('');
  div.querySelectorAll('button').forEach(function(b) {
    b.addEventListener('click', function() { soSerials.splice(parseInt(b.dataset.i),1); renderSoTags(); });
  });
}
