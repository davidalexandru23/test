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

    // Simple chatbot for Opel information
    const chatForm = document.getElementById('chat-form');
    const chatInput = document.getElementById('chat-message');
    const chatBody = document.getElementById('chat-body');

    if (chatForm && chatInput && chatBody) {
        chatForm.addEventListener('submit', e => {
            e.preventDefault();
            const message = chatInput.value.trim();
            if (!message) return;
            addChat('You', message);
            addChat('Bot', getReply(message));
            chatInput.value = '';
        });
    }

    function addChat(sender, text) {
        const p = document.createElement('p');
        p.textContent = sender + ': ' + text;
        chatBody.appendChild(p);
        chatBody.scrollTop = chatBody.scrollHeight;
    }

    function getReply(msg) {
        const m = msg.toLowerCase();
        if (m.includes('astra')) {
            return 'Opel Astra H este cunoscut pentru fiabilitate si manevrabilitate.';
        }
        if (m.includes('motor')) {
            return 'Astra H a fost echipat cu motoare precum benzina 1.6 si diesel 1.7 CDTI.';
        }
        if (m.includes('consum')) {
            return 'Consumul pentru variantele diesel este in jur de 5-6 l/100 km.';
        }
        if (m.includes('pret')) {
            return 'Pretul variaza in functie de starea masinii si nivelul de dotari.';
        }
        return 'Imi place sa discut despre modelele Opel!';
    }
});
