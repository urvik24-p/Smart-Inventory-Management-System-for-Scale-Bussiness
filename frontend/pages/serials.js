/* pages/serials.js */

async function renderSerials(container) {
  container.innerHTML = '<div class="skeleton" style="height:400px;border-radius:10px;"></div>';
  try {
    var serials = await SerialAPI.getAll();
    container.innerHTML =
      '<div class="card">' +
        '<div class="card-header">' +
          '<h6><i class="bi bi-upc-scan"></i> Serial Numbers</h6>' +
          '<span style="font-size:11px;color:var(--text-muted)">' + serials.length + ' units</span>' +
        '</div>' +
        (serials.length === 0
          ? '<div style="padding:48px;text-align:center;color:var(--text-muted)">' +
              '<i class="bi bi-upc" style="font-size:36px;display:block;margin-bottom:10px;"></i>' +
              'No serial numbers yet. Use <strong>Stock In</strong> to add inventory.' +
            '</div>'
          : '<div style="overflow-x:auto"><table class="si-table">' +
              '<thead><tr><th>#</th><th>Serial Number</th><th>Product</th><th>Status</th><th>Created</th><th>Change Status</th></tr></thead>' +
              '<tbody>' +
              serials.map(function(sn) {
                var bdg = sn.status==='available' ? 'badge-avail' : sn.status==='sold' ? 'badge-sold' : 'badge-damaged';
                return '<tr>' +
                  '<td style="color:var(--text-muted);font-size:12px">#' + sn.serialId + '</td>' +
                  '<td><code style="background:var(--blue-light);color:var(--blue);padding:2px 8px;border-radius:5px;font-size:12px;">' + escHtml(sn.serialNumber) + '</code></td>' +
                  '<td style="font-weight:500">' + escHtml(sn.product?sn.product.productName:'—') + '</td>' +
                  '<td><span class="' + bdg + '">' + sn.status + '</span></td>' +
                  '<td style="color:var(--text-muted);font-size:12px">' + formatDateShort(sn.createdAt) + '</td>' +
                  '<td>' +
                    '<select class="form-input sn-sel" data-id="' + sn.serialId + '" style="width:130px;padding:5px 10px;font-size:12px;">' +
                      '<option value="available"' + (sn.status==='available'?' selected':'') + '>Available</option>' +
                      '<option value="sold"'      + (sn.status==='sold'     ?' selected':'') + '>Sold</option>' +
                      '<option value="damaged"'   + (sn.status==='damaged'  ?' selected':'') + '>Damaged</option>' +
                    '</select>' +
                  '</td>' +
                '</tr>';
              }).join('') +
              '</tbody></table></div>'
        ) +
      '</div>';

    document.querySelectorAll('.sn-sel').forEach(function(sel) {
      sel.addEventListener('change', async function() {
        try {
          await SerialAPI.updateStatus(sel.dataset.id, sel.value);
          showToast('Status updated to: ' + sel.value, 'success');
          renderSerials(container);
        } catch(err) {
          showToast(err.message, 'error');
          renderSerials(container);
        }
      });
    });
  } catch(err) {
    container.innerHTML = '<div style="padding:20px;color:var(--danger)">Error: ' + escHtml(err.message) + '</div>';
  }
}
