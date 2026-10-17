/* pages/inventory.js */

async function renderInventory(container) {
  container.innerHTML = '<div class="skeleton" style="height:400px;border-radius:10px;"></div>';
  try {
    var inv = await InventoryAPI.getAll();
    container.innerHTML =
      '<div class="card">' +
        '<div class="card-header">' +
          '<h6><i class="bi bi-clipboard-data-fill"></i> Current Stock Levels</h6>' +
          '<span style="font-size:11px;color:var(--text-muted)">' + inv.length + ' products</span>' +
        '</div>' +
        (inv.length === 0
          ? '<div style="padding:48px;text-align:center;color:var(--text-muted)">No inventory data yet.</div>'
          : '<div style="overflow-x:auto"><table class="si-table">' +
              '<thead><tr><th>Product</th><th>Category</th><th>Brand</th>' +
              '<th style="text-align:center">Available</th><th style="text-align:center">Sold</th>' +
              '<th style="text-align:center">Damaged</th><th style="text-align:center">Total</th>' +
              '<th>Stock Bar</th></tr></thead>' +
              '<tbody>' +
              inv.map(function(item) {
                var total = item.totalCount||0;
                var pct   = total>0 ? Math.round((item.availableCount/total)*100) : 0;
                return '<tr>' +
                  '<td style="font-weight:600">' + escHtml(item.productName) + '</td>' +
                  '<td>' + (item.category ? '<span class="badge-cat">' + escHtml(item.category) + '</span>' : '<span style="color:var(--text-muted)">—</span>') + '</td>' +
                  '<td style="color:var(--text-sec)">' + (escHtml(item.brand)||'—') + '</td>' +
                  '<td style="text-align:center;font-weight:700;color:var(--success)">' + (item.availableCount||0) + '</td>' +
                  '<td style="text-align:center;font-weight:600;color:var(--danger)">'  + (item.soldCount||0)     + '</td>' +
                  '<td style="text-align:center;font-weight:600;color:var(--warning)">' + (item.damagedCount||0)  + '</td>' +
                  '<td style="text-align:center;font-weight:700">' + total + '</td>' +
                  '<td style="min-width:100px;">' +
                    '<div class="prog-bar"><div class="prog-fill ' + (pct>30?'blue':'red') + '" style="width:' + pct + '%"></div></div>' +
                    '<span style="font-size:10px;color:var(--text-muted)">' + pct + '%</span>' +
                  '</td>' +
                '</tr>';
              }).join('') +
              '</tbody></table></div>'
        ) +
      '</div>';
  } catch(err) {
    container.innerHTML = '<div style="padding:20px;color:var(--danger)">Error: ' + escHtml(err.message) + '</div>';
  }
}
