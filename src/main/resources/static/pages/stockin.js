/* pages/stockin.js — Serial or Quantity mode */

var siSerials = [];

async function renderStockIn(container) {
  siSerials = [];
  var products = [];
  try { products = await ProductAPI.getAll(); }
  catch(err) { container.innerHTML = '<div style="color:var(--danger);padding:20px">' + escHtml(err.message) + '</div>'; return; }

  container.innerHTML =
    '<div style="max-width:640px;">' +
    '<div class="card">' +
      '<div class="card-header"><h6><i class="bi bi-box-arrow-in-down"></i> Receive New Stock</h6></div>' +
      '<div style="padding:20px;">' +
        '<form id="si-form">' +

          // Product
          '<div class="form-group">' +
            '<label class="form-label">Product *</label>' +
            '<select class="form-input" id="si-product" required>' +
              '<option value="">— Select Product —</option>' +
              products.map(function(p) {
                return '<option value="' + p.productId + '">' + escHtml(p.productName) + (p.brand ? ' · ' + escHtml(p.brand) : '') + '</option>';
              }).join('') +
            '</select>' +
          '</div>' +

          // Mode
          '<div class="form-group">' +
            '<label class="form-label">Entry Mode</label>' +
            '<div class="mode-toggle">' +
              '<button type="button" class="mode-btn active green-mode" data-m="serial"><i class="bi bi-upc-scan"></i> Serial Numbers</button>' +
              '<button type="button" class="mode-btn" data-m="qty"><i class="bi bi-123"></i> Quantity Only</button>' +
            '</div>' +
          '</div>' +

          // Serial panel
          '<div id="si-serial-panel">' +
            '<div class="form-group">' +
              '<label class="form-label">Serial Numbers <span class="opt">— scan or type, press Enter</span></label>' +
              '<div style="display:flex;gap:8px;">' +
                '<input class="form-input" type="text" id="si-sn-input" placeholder="Type serial number..." style="flex:1">' +
                '<button type="button" class="btn-blue" id="si-add-btn"><i class="bi bi-plus-lg"></i> Add</button>' +
              '</div>' +
              '<div class="tags-area" id="si-tags"><span style="color:var(--text-muted);font-size:12px">None added</span></div>' +
            '</div>' +
          '</div>' +

          // Qty panel
          '<div id="si-qty-panel" style="display:none;">' +
            '<div class="form-group">' +
              '<label class="form-label">Quantity *</label>' +
              '<div style="display:flex;align-items:center;gap:12px;">' +
                '<div class="qty-wrap">' +
                  '<button type="button" class="qty-btn" id="si-minus">−</button>' +
                  '<span class="qty-val" id="si-qty-val">1</span>' +
                  '<button type="button" class="qty-btn" id="si-plus">+</button>' +
                '</div>' +
                '<input class="form-input" type="number" id="si-qty" value="1" min="1" style="width:90px;">' +
              '</div>' +
              '<p style="font-size:11px;color:var(--text-muted);margin-top:6px;"><i class="bi bi-info-circle"></i> Units added without individual serial tracking — good for routine entries.</p>' +
            '</div>' +
          '</div>' +

          // Reference
          '<div class="form-group">' +
            '<label class="form-label">Reference Note <span class="opt">(optional)</span></label>' +
            '<input class="form-input" type="text" id="si-note" placeholder="e.g. PO-2024-001, Supplier: XYZ Corp">' +
          '</div>' +

          '<button type="submit" class="btn-green" style="width:100%;justify-content:center;padding:10px;">' +
            '<i class="bi bi-box-arrow-in-down"></i> Add to Stock' +
          '</button>' +

        '</form>' +
      '</div>' +
    '</div>' +
    '</div>';

  var mode = 'serial', qty = 1;

  // Mode toggle
  document.querySelectorAll('.mode-btn').forEach(function(btn) {
    btn.addEventListener('click', function() {
      mode = btn.dataset.m;
      document.querySelectorAll('.mode-btn').forEach(function(b) {
        b.classList.remove('active','green-mode');
      });
      btn.classList.add('active','green-mode');
      document.getElementById('si-serial-panel').style.display = mode==='serial' ? '' : 'none';
      document.getElementById('si-qty-panel').style.display    = mode==='qty'    ? '' : 'none';
    });
  });

  // Qty stepper
  document.getElementById('si-plus').addEventListener('click', function() { qty = Math.min(9999,qty+1); sync(); });
  document.getElementById('si-minus').addEventListener('click', function() { qty = Math.max(1,qty-1);   sync(); });
  document.getElementById('si-qty').addEventListener('input', function() { qty = Math.max(1,parseInt(this.value)||1); sync(); });
  function sync() { document.getElementById('si-qty').value = qty; document.getElementById('si-qty-val').textContent = qty; }

  // Add serial
  document.getElementById('si-add-btn').addEventListener('click', addSiSerial);
  document.getElementById('si-sn-input').addEventListener('keydown', function(e) { if(e.key==='Enter'){e.preventDefault();addSiSerial();} });

  // Submit
  document.getElementById('si-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    var pid  = document.getElementById('si-product').value;
    var note = document.getElementById('si-note').value.trim();
    if (!pid) { showToast('Select a product', 'error'); return; }
    try {
      if (mode === 'serial') {
        if (siSerials.length === 0) { showToast('Add at least one serial number', 'error'); return; }
        await StockInAPI.process(parseInt(pid), siSerials, note);
        showToast(siSerials.length + ' unit(s) added to stock!', 'success');
      } else {
        qty = parseInt(document.getElementById('si-qty').value)||1;
        var auto = [];
        var ts = Date.now();
        for (var i=0; i<qty; i++) auto.push('QTY-'+ts+'-'+(i+1));
        await StockInAPI.process(parseInt(pid), auto, note||'Bulk entry');
        showToast(qty + ' unit(s) added!', 'success');
      }
      siSerials = [];
      renderStockIn(container);
    } catch(err) { showToast(err.message, 'error'); }
  });
}

function addSiSerial() {
  var inp = document.getElementById('si-sn-input');
  var val = inp.value.trim();
  if (!val) return;
  if (siSerials.includes(val)) { showToast('Already added', 'error'); return; }
  siSerials.push(val);
  inp.value = ''; inp.focus();
  renderSiTags();
}

function renderSiTags() {
  var div = document.getElementById('si-tags');
  if (siSerials.length === 0) { div.innerHTML = '<span style="color:var(--text-muted);font-size:12px">None added</span>'; return; }
  div.innerHTML = siSerials.map(function(sn, i) {
    return '<span class="serial-tag">' + escHtml(sn) + '<button data-i="' + i + '">✕</button></span>';
  }).join('');
  div.querySelectorAll('button').forEach(function(b) {
    b.addEventListener('click', function() { siSerials.splice(parseInt(b.dataset.i),1); renderSiTags(); });
  });
}
