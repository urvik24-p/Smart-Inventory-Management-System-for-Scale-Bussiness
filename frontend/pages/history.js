/* pages/history.js */

async function renderHistory(container) {
  container.innerHTML =
    // Filter bar
    '<div class="card" style="margin-bottom:14px;">' +
      '<div style="padding:14px 18px;display:flex;gap:10px;align-items:flex-end;flex-wrap:wrap;">' +
        '<div><label class="form-label">Type</label>' +
          '<select class="form-input" id="hf-type" style="width:130px;">' +
            '<option value="">All Types</option>' +
            '<option value="IN">Stock In</option>' +
            '<option value="OUT">Stock Out</option>' +
          '</select></div>' +
        '<div><label class="form-label">From</label>' +
          '<input class="form-input" type="date" id="hf-from" style="width:150px;"></div>' +
        '<div><label class="form-label">To</label>' +
          '<input class="form-input" type="date" id="hf-to" style="width:150px;"></div>' +
        '<button class="btn-blue" id="hf-filter"><i class="bi bi-funnel"></i> Filter</button>' +
        '<button class="btn-ghost" id="hf-clear">Clear</button>' +
      '</div>' +
    '</div>' +
    '<div id="hist-table"></div>' +

    // Modal
    '<div class="modal fade" id="txModal" tabindex="-1">' +
      '<div class="modal-dialog modal-dialog-centered modal-lg">' +
        '<div class="modal-content">' +
          '<div class="modal-header">' +
            '<span class="modal-title" id="tx-modal-title">Transaction Details</span>' +
            '<button type="button" class="btn-close" data-bs-dismiss="modal"></button>' +
          '</div>' +
          '<div class="modal-body" id="tx-modal-body"></div>' +
        '</div>' +
      '</div>' +
    '</div>';

  loadHistTable({});

  document.getElementById('hf-filter').addEventListener('click', function() {
    loadHistTable({
      type: document.getElementById('hf-type').value||undefined,
      from: document.getElementById('hf-from').value||undefined,
      to:   document.getElementById('hf-to').value||undefined,
    });
  });
  document.getElementById('hf-clear').addEventListener('click', function() {
    document.getElementById('hf-type').value='';
    document.getElementById('hf-from').value='';
    document.getElementById('hf-to').value='';
    loadHistTable({});
  });
}

async function loadHistTable(params) {
  var div = document.getElementById('hist-table');
  div.innerHTML = '<div class="skeleton" style="height:300px;border-radius:10px;"></div>';
  try {
    var history = await HistoryAPI.getAll(params);
    div.innerHTML =
      '<div class="card">' +
        '<div class="card-header">' +
          '<h6><i class="bi bi-clock-history"></i> Transaction Log</h6>' +
          '<span style="font-size:11px;color:var(--text-muted)">' + history.length + ' records</span>' +
        '</div>' +
        '<div style="overflow-x:auto"><table class="si-table">' +
          '<thead><tr><th>#</th><th>Type</th><th>Reference</th><th>Units</th><th>Date</th><th style="text-align:right">Action</th></tr></thead>' +
          '<tbody>' +
          (history.length===0
            ? '<tr><td colspan="6" style="text-align:center;padding:36px;color:var(--text-muted)">No transactions found.</td></tr>'
            : history.map(function(tx) {
                return '<tr>' +
                  '<td style="color:var(--text-muted);font-size:12px">#' + tx.transactionId + '</td>' +
                  '<td><span class="' + (tx.transactionType==='IN'?'badge-in':'badge-out') + '">' + (tx.transactionType==='IN'?'↓ IN':'↑ OUT') + '</span></td>' +
                  '<td style="max-width:200px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + (escHtml(tx.referenceNote)||'<span style="color:var(--text-muted)">—</span>') + '</td>' +
                  '<td>' + (tx.items?tx.items.length:0) + ' unit(s)</td>' +
                  '<td style="color:var(--text-muted);font-size:12px">' + formatDate(tx.createdAt) + '</td>' +
                  '<td style="text-align:right"><button class="btn-ghost btn-view-tx" style="font-size:12px;padding:5px 10px;" data-id="' + tx.transactionId + '"><i class="bi bi-eye"></i> View</button></td>' +
                '</tr>';
              }).join('')
          ) +
          '</tbody></table></div>' +
      '</div>';

    document.querySelectorAll('.btn-view-tx').forEach(function(btn) {
      btn.addEventListener('click', function() { showTxDetail(btn.dataset.id); });
    });
  } catch(err) {
    div.innerHTML = '<div style="padding:20px;color:var(--danger)">Error: ' + escHtml(err.message) + '</div>';
  }
}

async function showTxDetail(id) {
  var modal = new bootstrap.Modal(document.getElementById('txModal'));
  var body  = document.getElementById('tx-modal-body');
  document.getElementById('tx-modal-title').textContent = 'Transaction #' + id;
  body.innerHTML = '<div class="skeleton" style="height:180px;border-radius:8px;"></div>';
  modal.show();
  try {
    var tx = await HistoryAPI.getById(id);
    body.innerHTML =
      '<div style="display:grid;grid-template-columns:repeat(3,1fr);gap:12px;margin-bottom:18px;">' +
        info('ID', '#' + tx.transactionId) +
        info('Type', '<span class="' + (tx.transactionType==='IN'?'badge-in':'badge-out') + '">' + (tx.transactionType==='IN'?'↓ Stock In':'↑ Stock Out') + '</span>') +
        info('Date', formatDate(tx.createdAt)) +
      '</div>' +
      info('Reference Note', escHtml(tx.referenceNote)||'—') +
      '<hr style="border-color:var(--border);margin:16px 0;">' +
      '<p style="font-size:12px;font-weight:700;color:var(--text-sec);text-transform:uppercase;letter-spacing:0.6px;margin-bottom:10px;">Items (' + (tx.items?tx.items.length:0) + ' units)</p>' +
      '<table class="si-table" style="background:#f8fafc;border-radius:8px;overflow:hidden;">' +
        '<thead><tr><th>Product</th><th>Serial Number</th><th style="text-align:center">Qty</th></tr></thead>' +
        '<tbody>' +
        (tx.items&&tx.items.length>0
          ? tx.items.map(function(item) {
              return '<tr>' +
                '<td>' + escHtml(item.product?item.product.productName:'—') + '</td>' +
                '<td><code style="background:var(--blue-light);color:var(--blue);padding:2px 7px;border-radius:5px;font-size:12px;">' + escHtml(item.serialNumber?item.serialNumber.serialNumber:'—') + '</code></td>' +
                '<td style="text-align:center;font-weight:600">' + item.quantity + '</td>' +
              '</tr>';
            }).join('')
          : '<tr><td colspan="3" style="text-align:center;padding:20px;color:var(--text-muted)">No items</td></tr>'
        ) +
        '</tbody></table>';
  } catch(err) {
    body.innerHTML = '<div style="color:var(--danger)">' + escHtml(err.message) + '</div>';
  }
}

function info(label, value) {
  return '<div style="background:#f8fafc;border:1px solid var(--border);border-radius:7px;padding:10px 12px;">' +
    '<div style="font-size:10px;font-weight:700;text-transform:uppercase;letter-spacing:0.6px;color:var(--text-muted);margin-bottom:4px;">' + label + '</div>' +
    '<div style="font-size:13px;font-weight:600;color:var(--text)">' + value + '</div>' +
  '</div>';
}
