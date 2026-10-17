/* pages/dbviewer.js — Live database tables viewer */

async function renderDbViewer(container) {
  container.innerHTML =
    '<div class="card">' +
      '<div class="card-header">' +
        '<h6><i class="bi bi-database-fill"></i> Live Database Viewer</h6>' +
        '<button class="btn-ghost" id="db-refresh" style="font-size:12px;padding:5px 11px;"><i class="bi bi-arrow-clockwise"></i> Refresh</button>' +
      '</div>' +
      '<div class="db-tabs">' +
        '<button class="db-tab active" data-t="products">📦 Products</button>' +
        '<button class="db-tab" data-t="serials">🔖 Serials</button>' +
        '<button class="db-tab" data-t="transactions">📋 Transactions</button>' +
        '<button class="db-tab" data-t="inventory">📊 Inventory</button>' +
      '</div>' +
      '<div id="db-content" style="overflow-x:auto;min-height:180px;"></div>' +
    '</div>';

  var current = 'products';

  async function load(table) {
    current = table;
    var div = document.getElementById('db-content');
    div.innerHTML = '<div style="padding:20px;"><div class="skeleton" style="height:200px;border-radius:8px;"></div></div>';
    try {
      var rows, heads, cells;

      if (table === 'products') {
        var data = await ProductAPI.getAll();
        heads = ['#ID','Name','Category','Brand','Created'];
        rows  = data.map(function(p) { return [
          '<span style="color:var(--text-muted)">#' + p.productId + '</span>',
          '<strong>' + escHtml(p.productName) + '</strong>',
          p.category ? '<span class="badge-cat">' + escHtml(p.category) + '</span>' : '—',
          escHtml(p.brand)||'—',
          '<span style="font-size:11px;color:var(--text-muted)">' + formatDateShort(p.createdAt) + '</span>',
        ]; });

      } else if (table === 'serials') {
        var data = await SerialAPI.getAll();
        heads = ['#ID','Product','Serial Number','Status','Created'];
        rows  = data.map(function(s) { return [
          '<span style="color:var(--text-muted)">#' + s.serialId + '</span>',
          escHtml(s.product?s.product.productName:'—'),
          '<code style="background:var(--blue-light);color:var(--blue);padding:2px 7px;border-radius:5px;font-size:12px;">' + escHtml(s.serialNumber) + '</code>',
          '<span class="' + (s.status==='available'?'badge-avail':s.status==='sold'?'badge-sold':'badge-damaged') + '">' + s.status + '</span>',
          '<span style="font-size:11px;color:var(--text-muted)">' + formatDateShort(s.createdAt) + '</span>',
        ]; });

      } else if (table === 'transactions') {
        var data = await HistoryAPI.getAll();
        heads = ['#ID','Type','Reference Note','Units','Date'];
        rows  = data.map(function(tx) { return [
          '<span style="color:var(--text-muted)">#' + tx.transactionId + '</span>',
          '<span class="' + (tx.transactionType==='IN'?'badge-in':'badge-out') + '">' + (tx.transactionType==='IN'?'↓ IN':'↑ OUT') + '</span>',
          escHtml(tx.referenceNote)||'—',
          (tx.items?tx.items.length:0) + ' units',
          '<span style="font-size:11px;color:var(--text-muted)">' + formatDate(tx.createdAt) + '</span>',
        ]; });

      } else {
        var data = await InventoryAPI.getAll();
        heads = ['Product','Category','Brand','Available','Sold','Damaged','Total'];
        rows  = data.map(function(i) { return [
          '<strong>' + escHtml(i.productName) + '</strong>',
          i.category ? '<span class="badge-cat">' + escHtml(i.category) + '</span>' : '—',
          escHtml(i.brand)||'—',
          '<span style="color:var(--success);font-weight:700">' + (i.availableCount||0) + '</span>',
          '<span style="color:var(--danger);font-weight:600">'  + (i.soldCount||0)     + '</span>',
          '<span style="color:var(--warning);font-weight:600">' + (i.damagedCount||0)  + '</span>',
          '<strong>' + (i.totalCount||0) + '</strong>',
        ]; });
      }

      div.innerHTML =
        '<div style="padding:8px 16px;font-size:11px;color:var(--text-muted);border-bottom:1px solid var(--border);background:#f8fafc;">' +
          '<i class="bi bi-table me-1"></i>' + rows.length + ' row' + (rows.length!==1?'s':'') +
        '</div>' +
        '<table class="si-table">' +
          '<thead><tr>' + heads.map(function(h){return '<th>'+h+'</th>';}).join('') + '</tr></thead>' +
          '<tbody>' +
          (rows.length===0
            ? '<tr><td colspan="' + heads.length + '" style="text-align:center;padding:36px;color:var(--text-muted)">Empty table</td></tr>'
            : rows.map(function(cells) {
                return '<tr>' + cells.map(function(c){return '<td>'+c+'</td>';}).join('') + '</tr>';
              }).join('')
          ) +
          '</tbody></table>';

    } catch(err) {
      div.innerHTML = '<div style="padding:20px;color:var(--danger)"><i class="bi bi-exclamation-circle"></i> ' + escHtml(err.message) + '</div>';
    }
  }

  document.querySelectorAll('.db-tab').forEach(function(tab) {
    tab.addEventListener('click', function() {
      document.querySelectorAll('.db-tab').forEach(function(t){t.classList.remove('active');});
      tab.classList.add('active');
      load(tab.dataset.t);
    });
  });

  document.getElementById('db-refresh').addEventListener('click', function() {
    load(current);
    showToast('Refreshed!', 'success');
  });

  load('products');
}
