/* js/app.js — Main router & utilities */

if (!localStorage.getItem('token')) window.location.href = 'index.html';

// User info
var username = localStorage.getItem('username') || 'User';
var role     = localStorage.getItem('role') || 'STAFF';
document.getElementById('sidebar-username').textContent = username;
document.getElementById('sidebar-role').textContent     = role;
document.getElementById('user-avatar').textContent      = username.charAt(0).toUpperCase();

// Logout
document.getElementById('btn-logout').addEventListener('click', function() {
  localStorage.clear();
  window.location.href = 'index.html';
});

// Mobile sidebar
function openSidebar()  { document.getElementById('sidebar').classList.add('open'); document.getElementById('sidebar-overlay').style.display = 'block'; }
function closeSidebar() { document.getElementById('sidebar').classList.remove('open'); document.getElementById('sidebar-overlay').style.display = 'none'; }
document.getElementById('menu-toggle').addEventListener('click', openSidebar);

// Page metadata
var pageMeta = {
  'dashboard': { title: 'Dashboard',         subtitle: 'Your inventory at a glance' },
  'products':  { title: 'Products',           subtitle: 'Manage your product catalogue' },
  'stock-in':  { title: 'Stock In',           subtitle: 'Add new inventory to stock' },
  'stock-out': { title: 'Stock Out',          subtitle: 'Dispatch inventory to customers' },
  'serials':   { title: 'Serial Numbers',     subtitle: 'Track individual units' },
  'inventory': { title: 'Current Inventory',  subtitle: 'Live stock levels by product' },
  'history':   { title: 'Transaction History','subtitle': 'Full log of all stock movements' },
  'db-viewer': { title: 'Database Viewer',    subtitle: 'Live view of all database tables' },
};

// Navigate
function navigateTo(page) {
  var meta = pageMeta[page] || { title: page, subtitle: '' };
  document.getElementById('page-title').textContent    = meta.title;
  document.getElementById('page-subtitle').textContent = meta.subtitle;
  document.getElementById('top-actions').innerHTML     = '';
  var body = document.getElementById('page-body');
  body.innerHTML = '';

  switch(page) {
    case 'dashboard':  renderDashboard(body);  break;
    case 'products':   renderProducts(body);   break;
    case 'stock-in':   renderStockIn(body);    break;
    case 'stock-out':  renderStockOut(body);   break;
    case 'serials':    renderSerials(body);    break;
    case 'inventory':  renderInventory(body);  break;
    case 'history':    renderHistory(body);    break;
    case 'db-viewer':  renderDbViewer(body);   break;
    default: body.innerHTML = '<p style="color:var(--text-sec)">Page not found.</p>';
  }
  closeSidebar();
}

// Sidebar clicks
document.querySelectorAll('.nav-link[data-page]').forEach(function(link) {
  link.addEventListener('click', function(e) {
    e.preventDefault();
    document.querySelectorAll('.nav-link[data-page]').forEach(function(l) { l.classList.remove('active'); });
    link.classList.add('active');
    navigateTo(link.dataset.page);
  });
});

// Toast
function showToast(message, type) {
  type = type || 'info';
  var icons = { success: 'bi-check-circle-fill', error: 'bi-exclamation-circle-fill', info: 'bi-info-circle-fill' };
  var el = document.createElement('div');
  el.className = 'si-toast ' + type;
  el.innerHTML =
    '<i class="bi ' + (icons[type]||icons.info) + ' t-icon ' + type + '"></i>' +
    '<span style="flex:1">' + escHtml(message) + '</span>' +
    '<button class="t-close" onclick="this.parentElement.remove()"><i class="bi bi-x"></i></button>';
  document.getElementById('toast-container').appendChild(el);
  setTimeout(function() { if (el.parentElement) el.remove(); }, 3500);
}

// Utilities
function formatDate(d) {
  if (!d) return '—';
  var dt = new Date(d);
  return dt.toLocaleDateString('en-IN', { day:'2-digit', month:'short', year:'numeric' })
       + ', ' + dt.toLocaleTimeString('en-IN', { hour:'2-digit', minute:'2-digit', hour12:true });
}
function formatDateShort(d) {
  if (!d) return '—';
  return new Date(d).toLocaleDateString('en-IN', { day:'2-digit', month:'short', year:'numeric' });
}
function escHtml(s) {
  if (!s) return '';
  return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;');
}

// Start
navigateTo('dashboard');
