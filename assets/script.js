// Search functionality for catalog
window.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('search');
    const partsList = document.getElementById('parts-list');
    if (searchInput && partsList) {
        searchInput.addEventListener('input', () => {
            const term = searchInput.value.toLowerCase();
            Array.from(partsList.children).forEach(li => {
                const name = li.getAttribute('data-name').toLowerCase();
                li.style.display = name.includes(term) ? '' : 'none';
            });
        });
    }

    // Fan page post handling
    const form = document.getElementById('fan-form');
    const posts = document.getElementById('fan-posts');
    if (form && posts) {
        form.addEventListener('submit', evt => {
            evt.preventDefault();
            const name = document.getElementById('fan-name').value.trim();
            const msg = document.getElementById('fan-message').value.trim();
            if (name && msg) {
                const li = document.createElement('li');
                li.textContent = name + ': ' + msg;
                posts.appendChild(li);
                form.reset();
            }
        });
    }
});
