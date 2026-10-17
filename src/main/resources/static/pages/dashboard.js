/* pages/dashboard.js */

async function renderDashboard(container) {
  container.innerHTML =
    '<div class="stat-grid">' +
      [1,2,3,4].map(() => '<div class="stat-card"><div class="skeleton" style="height:80px"></div></div>').join('') +
    '</div>' +
    '<div class="skeleton" style="height:320px;border-radius:10px;"></div>';

  try {
    var inv     = await InventoryAPI.getAll();
    var history = await HistoryAPI.getAll();

    var totalProd = inv.length, avail = 0, sold = 0, damaged = 0;
    inv.forEach(function(i) { avail += i.availableCount||0; sold += i.soldCount||0; damaged += i.damagedCount||0; });

    container.innerHTML =
      // Stat cards
      '<div class="stat-grid">' +
        statCard('blue',   'bi-tags-fill',          totalProd, 'Total Products') +
        statCard('green',  'bi-check-circle-fill',  avail,     'Available Units') +
        statCard('red',    'bi-cart-check-fill',    sold,      'Dispatched') +
        statCard('orange', 'bi-exclamation-triangle-fill', damaged, 'Damaged') +
      '</div>' +

      // Two columns
      '<div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">' +

        // Recent transactions
        '<div class="card">' +
          '<div class="card-header"><h6><i class="bi bi-clock-history"></i> Recent Transactions</h6>' +
            '<span style="font-size:11px;color:var(--text-muted)">' + history.length + ' total</span></div>' +
          '<div style="overflow-x:auto">' +
          '<table class="si-table">' +
            '<thead><tr><th>Type</th><th>Reference</th><th>Units</th><th>Date</th></tr></thead>' +
            '<tbody>' +
            (history.length === 0
              ? '<tr><td colspan="4" style="text-align:center;padding:32px;color:var(--text-muted)">No transactions yet</td></tr>'
              : history.slice(0,6).map(function(tx) {
                  return '<tr>' +
                    '<td><span class="' + (tx.transactionType==='IN'?'badge-in':'badge-out') + '">' + (tx.transactionType==='IN'?'↓ IN':'↑ OUT') + '</span></td>' +
                    '<td style="max-width:160px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + (escHtml(tx.referenceNote)||'<span style="color:var(--text-muted)">—</span>') + '</td>' +
                    '<td>' + (tx.items?tx.items.length:0) + '</td>' +
                    '<td style="color:var(--text-muted);font-size:11px;">' + formatDateShort(tx.createdAt) + '</td>' +
                  '</tr>';
                }).join('')
            ) +
            '</tbody></table></div></div>' +

        // Stock levels
        '<div class="card">' +
          '<div class="card-header"><h6><i class="bi bi-bar-chart-fill"></i> Stock Levels</h6></div>' +
          '<div style="padding:4px 0;">' +
          (inv.length === 0
            ? '<div style="padding:32px;text-align:center;color:var(--text-muted)">No products yet</div>'
            : inv.slice(0,8).map(function(item) {
                var total = item.totalCount||0;
                var pct   = total > 0 ? Math.round((item.availableCount/total)*100) : 0;
                return '<div style="padding:10px 16px;border-bottom:1px solid var(--border);">' +
                  '<div style="display:flex;justify-content:space-between;margin-bottom:5px;">' +
                    '<span style="font-size:12px;font-weight:600;">' + escHtml(item.productName) + '</span>' +
                    '<span style="font-size:11px;color:var(--text-muted)">' + (item.availableCount||0) + ' / ' + total + '</span>' +
                  '</div>' +
                  '<div class="prog-bar"><div class="prog-fill ' + (pct > 30 ? 'blue' : 'red') + '" style="width:' + pct + '%"></div></div>' +
                '</div>';
              }).join('')
          ) +
          '</div></div>' +

      '</div>';

  } catch(err) {
    container.innerHTML = '<div style="color:var(--danger);padding:20px">Failed to load: ' + escHtml(err.message) + '</div>';
  }
}

function statCard(type, icon, value, label) {
  return '<div class="stat-card">' +
    '<div class="stat-icon ' + type + '"><i class="bi ' + icon + '"></i></div>' +
    '<div class="stat-value">' + value + '</div>' +
    '<div class="stat-label">' + label + '</div>' +
  '</div>';
}
