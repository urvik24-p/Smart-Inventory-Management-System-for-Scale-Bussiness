/* ============================================
   API HELPER — api.js
   ============================================
   
   All HTTP calls to the Spring Boot backend.
   
   How it works:
     1. Every function calls apiFetch()
     2. apiFetch() reads the JWT token from localStorage
     3. Adds "Authorization: Bearer <token>" header
     4. Returns parsed JSON or throws an error
   
   Backend runs at: http://localhost:8081/api
   ============================================ */

const API_BASE = (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')
    ? 'http://localhost:8081/api'
    : (localStorage.getItem('BACKEND_URL') || 'https://smartinventory-backend.onrender.com/api');


// ============================================
// GENERIC FETCH WRAPPER
// Used by all API functions below
// ============================================

async function apiFetch(endpoint, options = {}) {

    // Read JWT token from localStorage
    const token = localStorage.getItem('token');

    // Set default headers
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers,
    };

    // Attach token if available
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    // Make the request
    const response = await fetch(API_BASE + endpoint, {
        ...options,
        headers,
    });

    // 204 No Content (e.g., after DELETE)
    if (response.status === 204) {
        return null;
    }

    // Try to parse JSON
    let data;
    try {
        data = await response.json();
    } catch (e) {
        // If response is not JSON (e.g., plain text from register)
        if (response.ok) return null;
        throw new Error('Request failed');
    }

    // If HTTP status is not OK, throw the error message
    if (!response.ok) {
        throw new Error(data.message || 'Something went wrong');
    }

    return data;
}


// ============================================
// AUTH ENDPOINTS
// POST /api/auth/login
// POST /api/auth/register
// ============================================

const AuthAPI = {

    login: (username, password) =>
        apiFetch('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password }),
        }),

    register: (username, password, role) =>
        apiFetch('/auth/register', {
            method: 'POST',
            body: JSON.stringify({ username, password, role }),
        }),
};


// ============================================
// PRODUCT ENDPOINTS
// GET    /api/products
// GET    /api/products/{id}
// POST   /api/products
// PUT    /api/products/{id}
// DELETE /api/products/{id}
// ============================================

const ProductAPI = {

    getAll:           () => apiFetch('/products'),
    getById:        (id) => apiFetch('/products/' + id),
    add:       (product) => apiFetch('/products', { method: 'POST', body: JSON.stringify(product) }),
    update: (id, product) => apiFetch('/products/' + id, { method: 'PUT', body: JSON.stringify(product) }),
    delete:         (id) => apiFetch('/products/' + id, { method: 'DELETE' }),
};


// ============================================
// SERIAL NUMBER ENDPOINTS
// GET    /api/serial-numbers
// GET    /api/serial-numbers/product/{productId}
// GET    /api/serial-numbers/product/{pid}/status/{status}
// POST   /api/serial-numbers/product/{productId}
// PATCH  /api/serial-numbers/{id}/status
// DELETE /api/serial-numbers/{id}
// ============================================

const SerialAPI = {

    getAll:                        () => apiFetch('/serial-numbers'),
    getByProduct:       (productId) => apiFetch('/serial-numbers/product/' + productId),
    getByProductAndStatus: (pid, s) => apiFetch('/serial-numbers/product/' + pid + '/status/' + s),
    add:    (productId, serialNumber) =>
        apiFetch('/serial-numbers/product/' + productId, {
            method: 'POST',
            body: JSON.stringify({ serialNumber }),
        }),
    updateStatus:       (id, status) =>
        apiFetch('/serial-numbers/' + id + '/status', {
            method: 'PATCH',
            body: JSON.stringify({ status }),
        }),
    delete:                    (id) => apiFetch('/serial-numbers/' + id, { method: 'DELETE' }),
};


// ============================================
// STOCK IN ENDPOINT
// POST /api/stock-in
// ============================================

const StockInAPI = {

    process: (productId, serialNumbers, referenceNote) =>
        apiFetch('/stock-in', {
            method: 'POST',
            body: JSON.stringify({ productId, serialNumbers, referenceNote }),
        }),
};


// ============================================
// STOCK OUT ENDPOINT
// POST /api/stock-out
// ============================================

const StockOutAPI = {

    process: (productId, serialNumbers, referenceNote) =>
        apiFetch('/stock-out', {
            method: 'POST',
            body: JSON.stringify({ productId, serialNumbers, referenceNote }),
        }),
};


// ============================================
// INVENTORY ENDPOINTS
// GET /api/inventory
// GET /api/inventory/{productId}
// ============================================

const InventoryAPI = {

    getAll:             () => apiFetch('/inventory'),
    getByProduct:     (id) => apiFetch('/inventory/' + id),
};


// ============================================
// HISTORY ENDPOINTS
// GET /api/history
// GET /api/history?type=IN&from=2024-01-01&to=2024-12-31
// GET /api/history/{id}
// ============================================

const HistoryAPI = {

    getAll: (params = {}) => {
        const query = new URLSearchParams();
        if (params.type) query.append('type', params.type);
        if (params.from) query.append('from', params.from);
        if (params.to)   query.append('to', params.to);
        const qs = query.toString();
        return apiFetch('/history' + (qs ? '?' + qs : ''));
    },

    getById: (id) => apiFetch('/history/' + id),
};
