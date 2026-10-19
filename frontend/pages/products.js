/* pages/products.js */

async function renderProducts(container) {
  container.innerHTML = '<div class="skeleton" style="height:400px;border-radius:10px;"></div>';

  var canManageProducts = (localStorage.getItem('role') === 'ADMIN');

  document.getElementById('top-actions').innerHTML = canManageProducts
    ? '<button class="btn-blue" id="btn-add-product"><i class="bi bi-plus-lg"></i> Add Product</button>' : '';

  try {
    var products = await ProductAPI.getAll();

    container.innerHTML =
      '<div class="card">' +
        '<div class="card-header">' +
          '<h6><i class="bi bi-tags-fill"></i> Product Catalogue</h6>' +
          '<span style="font-size:11px;color:var(--text-muted)">' + products.length + ' products</span>' +
        '</div>' +
        (products.length === 0
          ? '<div style="padding:48px;text-align:center;color:var(--text-muted)">' +
              '<i class="bi bi-tags" style="font-size:36px;display:block;margin-bottom:10px;"></i>' +
              'No products yet. Click <strong>Add Product</strong> to start.' +
            '</div>'
          : '<div style="overflow-x:auto"><table class="si-table">' +
              '<thead><tr><th>#</th><th>Name</th><th>Category</th><th>Brand</th><th>Added</th><th style="text-align:right">Actions</th></tr></thead>' +
              '<tbody>' +
              products.map(function(p) {
                return '<tr>' +
                  '<td style="color:var(--text-muted);font-size:12px">#' + p.productId + '</td>' +
                  '<td style="font-weight:600">' + escHtml(p.productName) + '</td>' +
                  '<td>' + (p.category ? '<span class="badge-cat">' + escHtml(p.category) + '</span>' : '<span style="color:var(--text-muted)">—</span>') + '</td>' +
                  '<td style="color:var(--text-sec)">' + (escHtml(p.brand)||'—') + '</td>' +
                  '<td style="color:var(--text-muted);font-size:12px">' + formatDateShort(p.createdAt) + '</td>' +
                  '<td style="text-align:right">' + (canManageProducts ?
                    '<button class="btn-icon btn-edit-prod me-1" data-id="' + p.productId + '" title="Edit"><i class="bi bi-pencil"></i></button>' +
                    '<button class="btn-icon del btn-del-prod" data-id="' + p.productId + '" title="Delete"><i class="bi bi-trash"></i></button>' +
                  '' : '—') + '</td></tr>';
              }).join('') +
              '</tbody></table></div>'
        ) +
      '</div>' +

      // Modal
      '<div class="modal fade" id="prodModal" tabindex="-1">' +
        '<div class="modal-dialog modal-dialog-centered">' +
          '<div class="modal-content">' +
            '<div class="modal-header">' +
              '<span class="modal-title" id="prodModalTitle">Add Product</span>' +
              '<button type="button" class="btn-close" data-bs-dismiss="modal"></button>' +
            '</div>' +
            '<div class="modal-body">' +
              '<form id="prod-form">' +
                '<input type="hidden" id="pf-id">' +
                '<div class="form-group">' +
                  '<label class="form-label">Product Name *</label>' +
                  '<input class="form-input" type="text" id="pf-name" placeholder="e.g. pH Meter HI98130" required>' +
                '</div>' +
                '<div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;">' +
                  '<div class="form-group">' +
                    '<label class="form-label">Category</label>' +
                    '<input class="form-input" type="text" id="pf-category" placeholder="e.g. Testing Instruments" list="cat-list">' +
                    '<datalist id="cat-list">' +
                      '<option value="Testing Instruments">' +
                      '<option value="Measurement Instruments">' +
                      '<option value="Environmental Monitoring">' +
                      '<option value="Laboratory Equipment">' +
                      '<option value="Industrial Equipment">' +
                      '<option value="Weighing Scales">' +
                    '</datalist>' +
                  '</div>' +
                  '<div class="form-group">' +
                    '<label class="form-label">Brand</label>' +
                    '<input class="form-input" type="text" id="pf-brand" placeholder="e.g. Hanna Instruments" list="brand-list">' +
                    '<datalist id="brand-list">' +
                      '<option value="Hanna Instruments"><option value="Testo"><option value="Fluke"><option value="WIKA">' +
                      '<option value="Vaisala"><option value="Kern"><option value="Mettler Toledo"><option value="Lutron">' +
                    '</datalist>' +
                  '</div>' +
                '</div>' +
              '</form>' +
            '</div>' +
            '<div class="modal-footer">' +
              '<button type="button" class="btn-ghost" data-bs-dismiss="modal">Cancel</button>' +
              '<button type="button" class="btn-blue" id="pf-save">Save Product</button>' +
            '</div>' +
          '</div>' +
        '</div>' +
      '</div>';

    var modal = new bootstrap.Modal(document.getElementById('prodModal'));

    // Add button
    if (canManageProducts) document.getElementById('btn-add-product').addEventListener('click', function() {
      document.getElementById('prodModalTitle').textContent = 'Add Product';
      document.getElementById('pf-id').value = '';
      document.getElementById('pf-name').value = '';
      document.getElementById('pf-brand').value = '';
      document.getElementById('pf-category').value = '';
      modal.show();
    });

    // Edit
    document.querySelectorAll('.btn-edit-prod').forEach(function(btn) {
      btn.addEventListener('click', async function() {
        var p = await ProductAPI.getById(btn.dataset.id);
        document.getElementById('prodModalTitle').textContent = 'Edit Product';
        document.getElementById('pf-id').value       = p.productId;
        document.getElementById('pf-name').value     = p.productName;
        document.getElementById('pf-brand').value    = p.brand || '';
        document.getElementById('pf-category').value = p.category || '';
        modal.show();
      });
    });

    // Save
    document.getElementById('pf-save').addEventListener('click', async function() {
      var id   = document.getElementById('pf-id').value;
      var name = document.getElementById('pf-name').value.trim();
      if (!name) { showToast('Product name is required', 'error'); return; }
      var data = { productName: name, brand: document.getElementById('pf-brand').value.trim(), category: document.getElementById('pf-category').value.trim() };
      try {
        if (id) { await ProductAPI.update(id, data); showToast('Product updated!', 'success'); }
        else     { await ProductAPI.add(data);        showToast('Product added!', 'success'); }
        modal.hide();
        renderProducts(container);
      } catch(err) { showToast(err.message, 'error'); }
    });

    // Delete
    document.querySelectorAll('.btn-del-prod').forEach(function(btn) {
      btn.addEventListener('click', async function() {
        if (!confirm('Delete this product?')) return;
        try { await ProductAPI.delete(btn.dataset.id); showToast('Deleted', 'success'); renderProducts(container); }
        catch(err) { showToast(err.message, 'error'); }
      });
    });

  } catch(err) {
    container.innerHTML = '<div style="padding:20px;color:var(--danger)">Error: ' + escHtml(err.message) + '</div>';
  }
}
