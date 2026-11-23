async function apiFetch(url, options = {}) {
    const config = { credentials: 'same-origin', headers: { ...(options.headers || {}) }, ...options };
    if (!(options.body instanceof FormData) && !config.headers['Content-Type']) config.headers['Content-Type'] = 'application/json';
    const response = await fetch(url, config);
    const adminPages = ['dashboard','categories','items','customers','bookings','quotations','invoices','expenses','reports','inventory','settings'];
    if (response.status === 401 && adminPages.some(p => window.location.pathname.includes(p))) {
        window.location.href = '/login.html'; return null;
    }
    return response;
}

function renderAdminSidebar(active) {
    const el = document.getElementById('adminSidebar');
    if (!el) return;
    const links = [
        ['dashboard.html', 'bi-speedometer2', 'Dashboard'],
        ['categories.html', 'bi-tags', 'Categories'],
        ['items.html', 'bi-box-seam', 'Items & Pricing'],
        ['inventory.html', 'bi-arrow-left-right', 'Inventory Ledger'],
        ['customers.html', 'bi-people', 'Customers'],
        ['bookings.html', 'bi-calendar-check', 'Events & Bookings'],
        ['quotations.html', 'bi-file-earmark-text', 'Quotations (PDF)'],
        ['invoices.html', 'bi-receipt', 'Invoices & Payments'],
        ['expenses.html', 'bi-cash-stack', 'Expenses'],
        ['reports.html', 'bi-bar-chart-line', 'Reports & P&L'],
        ['settings.html', 'bi-gear', 'Business Settings']
    ];
    el.innerHTML = `<h5 class="fw-bold text-warning mb-3" data-biz-name>SRIAKAMATALLI TENT HOUSE</h5>`
        + links.map(l => `<a href="/${l[0]}" class="${active===l[0]?'active':''}"><i class="bi ${l[1]} me-2"></i>${l[2]}</a>`).join('')
        + `<a href="#" onclick="logoutAdmin()" class="text-danger mt-3"><i class="bi bi-box-arrow-left me-2"></i>Logout</a>`;
}

async function loadBusinessIdentity() {
    const res = await fetch('/api/public/business');
    if (!res.ok) return null;
    const b = await res.json();
    document.querySelectorAll('[data-biz-name]').forEach(e => e.textContent = b.businessName || '');
    document.querySelectorAll('[data-biz-tagline]').forEach(e => e.textContent = b.tagline || '');
    document.querySelectorAll('[data-biz-owners]').forEach(e => e.textContent = b.ownerNames || '');
    document.querySelectorAll('[data-biz-address]').forEach(e => e.textContent = [b.address, b.city, b.state].filter(Boolean).join(', '));
    document.querySelectorAll('[data-tel-1]').forEach(e => { e.textContent = b.primaryMobile; if (e.tagName==='A') e.href = 'tel:'+b.primaryMobile; });
    document.querySelectorAll('[data-tel-2]').forEach(e => { e.textContent = b.secondaryMobile||''; if (e.tagName==='A' && b.secondaryMobile) e.href = 'tel:'+b.secondaryMobile; });
    const wa1 = (b.whatsappPrimary || ('91'+b.primaryMobile)).replace(/\D/g,'');
    const wa2 = (b.whatsappSecondary || ('91'+(b.secondaryMobile||''))).replace(/\D/g,'');
    document.querySelectorAll('[data-wa-1]').forEach(e => e.href = `https://wa.me/${wa1}?text=${encodeURIComponent('Namaste, I would like to enquire about Tent House booking.')}`);
    document.querySelectorAll('[data-wa-2]').forEach(e => { if (wa2) e.href = `https://wa.me/${wa2}?text=${encodeURIComponent('Namaste, I would like to enquire about Tent House booking.')}`; });
    if (b.logoPath) document.querySelectorAll('[data-biz-logo]').forEach(i => { i.src = b.logoPath; i.classList.remove('d-none'); });
    return b;
}

async function logoutAdmin() {
    await fetch('/api/auth/logout', { method: 'POST', credentials: 'same-origin' });
    window.location.href = '/login.html';
}