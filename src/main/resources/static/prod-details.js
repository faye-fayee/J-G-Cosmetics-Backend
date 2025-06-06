let thumbnails = document.getElementsByClassName("thumbnail");
let activeImages = document.getElementsByClassName("active");


for (var i = 0; i < thumbnails.length; i++) {
    thumbnails[i].addEventListener("mouseover", function () {
        if (activeImages.length > 0) {
            activeImages[0].classList.remove("active");
        }
        this.classList.add("active");
        document.getElementById("featured").src = this.src;
    });
}

document.addEventListener("DOMContentLoaded", function () {
    updateCartCounter(document.querySelector(".cart-counter"));
    renderCartItems(); // ✅ Show cart items on page load
});

// Dynamic Creation of Product Details Page
document.addEventListener("DOMContentLoaded", async function () {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get("id");

    try {
        // Fetch product data from backend
        const response = await fetch(`http://34.105.57.169:8080/api/products/${productId}`);
        const product = await response.json();

        product.images = typeof product.images === "string" ? JSON.parse(product.images) : product.images;
        product.shades = typeof product.shades === "string" ? JSON.parse(product.shades) : product.shades;

        console.log(product); // Log the product data to verify the structure
        console.log("Parsed Shades:", product.shades);

        const hasShades = Array.isArray(product.shades) && product.shades.length > 0;

        
        if (product) {

            if (window.location.pathname.includes("prod-details.html")) {
                document.querySelector('title').textContent = product.name + " - J&G Cosmetics";
            }

            // Set title and description
            document.querySelector(".product-title").innerText = product.name;
            document.querySelector(".product-price").innerText = `₱${product.price}`;
            document.querySelector(".product-tagline").innerText = product.tagline;
            document.querySelector(".product-description").innerText = product.description;

            // Set featured image
            const featuredImg = document.getElementById("featured");
            if (product.images && product.images.length > 0) {
                featuredImg.src = product.images[0];
            }

            // Image slider (only if images are present)
            const slider = document.getElementById("slider");
            slider.innerHTML = "";  // Clear previous images if any
            if (product.images && product.images.length > 0) {
                product.images.forEach((imgSrc, index) => {
                    const img = document.createElement("img");
                    img.src = imgSrc;  // Using the image path from the database
                    img.alt = `Image ${index + 1}`;
                    img.classList.add("thumbnail");
                    if (index === 0) img.classList.add("active"); // Set the first image as active

                    // Change the featured image on hover
                    img.addEventListener("mouseover", function () {
                        featuredImg.src = imgSrc;
                        document.querySelector(".thumbnail.active")?.classList.remove("active");
                        img.classList.add("active");
                    });

                    slider.appendChild(img);
                });
            } else {
                console.log("No images found for this product.");
            }

            // Render shades if available
            const shadeContainer = document.querySelector(".shades-container");
            if (hasShades) {
                shadeContainer.innerHTML = "<h4>Shades</h4>";
                product.shades.forEach((shade) => {
                    const button = document.createElement("button");
                    button.innerText = shade;
                    button.classList.add("shade-btn");

                    button.addEventListener("click", (event) => {
                        document.querySelectorAll(".shade-btn").forEach((btn) => btn.classList.remove("selected"));
                        event.target.classList.add("selected");
                    });

                    shadeContainer.appendChild(button);
                });
                shadeContainer.style.display = "block";
            } else {
                shadeContainer.innerHTML = "";
                shadeContainer.style.display = "none";
            }



            //  Add to Cart Button
            const addToCartBtn = document.querySelector(".add-to-cart-btn");
            addToCartBtn.replaceWith(addToCartBtn.cloneNode(true));

            document.querySelector(".add-to-cart-btn").addEventListener("click", function () {
                const selectedShade = document.querySelector(".shade-btn.selected")?.innerText;
                const quantity = parseInt(document.querySelector(".add-to-cart input").value);

                if (hasShades && !selectedShade) {
                    alert("Please select a shade before adding to cart! 🎨");
                    return;
                }

                if (isNaN(quantity) || quantity <= 0) {
                    alert("Please enter a valid quantity.");
                    return;
                }

                const shadeToAdd = hasShades ? selectedShade : null;
                addToCart(product, shadeToAdd, quantity);

                console.log(JSON.parse(localStorage.getItem('cart')));

            });
        } else {
            document.querySelector(".product-details-container").innerHTML = "<h2>Product not found.</h2>";
        }
    } catch (error) {
        console.error("Error loading product data:", error);
    }

    // Load Cart Data on Page Load
    updateCartCounter(document.querySelector(".cart-counter"));
    renderCartItems(); // Show items when the cart is loaded
});
