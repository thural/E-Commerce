document.addEventListener('DOMContentLoaded', function() {
    // Initialize cart display state
    let isCartVisible = false;

    // Get DOM elements
    const cart = document.querySelector('.cart');
    const cartBackground = document.querySelector('.cart-bckg');
});

function toggleDisplay() {

console.log("toggle handler was clicked")
    const cart = document.querySelector('.cart');
    const cartBackground = document.querySelector('.cart-bckg');

    if (cart.style.display === 'none' || !cart.style.display) {
        cart.style.display = 'grid';
        cartBackground.style.display = 'block';
    } else {
        cart.style.display = 'none';
        cartBackground.style.display = 'none';
    }
}

function handleCart(itemId, action, currentCount) {
    const url = action === 'increment'
        ? `/cart/increment/${itemId}`
        : `/cart/decrement/${itemId}`;

    // Only allow decrement if count is greater than 0
    if (action === 'decrement' && currentCount <= 0) {
        return;
    }

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
        }
    })
    .then(response => response.json())
    .then(data => {
        // Update item count
        const itemElement = document.getElementById(`item-${itemId}`);
        const countElement = itemElement.querySelector('.counter p');
        const priceElement = itemElement.querySelector('.details p');

        countElement.textContent = data.count;
        priceElement.textContent = `$${(data.price * data.count).toFixed(2)}`;

        // Update total
        document.getElementById('cartTotal').textContent = data.cartTotal.toFixed(2);

        // Update badge
        document.querySelector('.cart-badge').textContent = data.totalItems;
    })
    .catch(error => console.error('Error:', error));
}

function checkout() {
    window.location.href = '/checkout';
}