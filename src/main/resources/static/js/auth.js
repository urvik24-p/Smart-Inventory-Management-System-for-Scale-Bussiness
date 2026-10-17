/* ============================================
   AUTH — auth.js
   ============================================
   
   Handles the login form on index.html.
   
   On submit:
     1. Sends POST /api/auth/login
     2. If success → saves token + user info to localStorage
     3. Redirects to dashboard.html
     4. If fail → shows error message
   ============================================ */

document.getElementById('login-form').addEventListener('submit', async function (e) {
    e.preventDefault();

    // Get form values
    const username = document.getElementById('login-username').value.trim();
    const password = document.getElementById('login-password').value;
    const errorDiv = document.getElementById('login-error');

    // Hide previous error
    errorDiv.classList.add('d-none');

    try {
        // Call the login API
        const data = await AuthAPI.login(username, password);

        // Save token and user info to localStorage
        localStorage.setItem('token', data.token);
        localStorage.setItem('username', data.username);
        localStorage.setItem('role', data.role);

        // Redirect to the main dashboard page
        window.location.href = 'dashboard.html';

    } catch (err) {
        // Show error message
        errorDiv.textContent = err.message || 'Login failed. Check your credentials.';
        errorDiv.classList.remove('d-none');
    }
});


// ============================================
// AUTO-REDIRECT
// If the user is already logged in, skip the login page
// ============================================

if (localStorage.getItem('token')) {
    window.location.href = 'dashboard.html';
}
