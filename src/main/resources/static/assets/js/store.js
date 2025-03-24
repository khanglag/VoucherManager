// Three.js Animation Script
let scene, camera, renderer;
let particles,
    geometry,
    materials = [],
    parameters;
let mouseX = 0,
    mouseY = 0;
let windowHalfX = window.innerWidth / 2;
let windowHalfY = window.innerHeight / 2;

init();
animate();

function init() {
    // Scene setup
    scene = new THREE.Scene();
    camera = new THREE.PerspectiveCamera(
        75,
        window.innerWidth / window.innerHeight,
        1,
        2000
    );
    camera.position.z = 1000;

    geometry = new THREE.BufferGeometry();
    const vertices = [];
    const textureLoader = new THREE.TextureLoader();

    for (let i = 0; i < 1000; i++) {
        const x = Math.random() * 2000 - 1000;
        const y = Math.random() * 2000 - 1000;
        const z = Math.random() * 2000 - 1000;
        vertices.push(x, y, z);
    }

    geometry.setAttribute(
        "position",
        new THREE.Float32BufferAttribute(vertices, 3)
    );

    parameters = [
        [[0.6, 0.8, 1], 5],
        [[0.4, 0.6, 1], 4],
        [[0.2, 0.4, 1], 3],
        [[0, 0.2, 1], 2],
        [[0, 0, 0.8], 1],
    ];

    for (let i = 0; i < parameters.length; i++) {
        const color = parameters[i][0];
        const size = parameters[i][1];

        materials[i] = new THREE.PointsMaterial({
            size: size,
            color: new THREE.Color(color[0], color[1], color[2]),
            transparent: true,
            opacity: 0.5,
        });

        const particles = new THREE.Points(geometry, materials[i]);
        particles.rotation.x = Math.random() * 6;
        particles.rotation.y = Math.random() * 6;
        particles.rotation.z = Math.random() * 6;
        scene.add(particles);
    }

    renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight);
    document
        .getElementById("canvas-container")
        .appendChild(renderer.domElement);

    document.addEventListener("mousemove", onDocumentMouseMove, false);
    window.addEventListener("resize", onWindowResize, false);
}

function onWindowResize() {
    windowHalfX = window.innerWidth / 2;
    windowHalfY = window.innerHeight / 2;
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
}

function onDocumentMouseMove(event) {
    mouseX = (event.clientX - windowHalfX) * 0.05;
    mouseY = (event.clientY - windowHalfY) * 0.05;
}

function animate() {
    requestAnimationFrame(animate);
    render();
}

function render() {
    camera.position.x += (mouseX - camera.position.x) * 0.05;
    camera.position.y += (-mouseY - camera.position.y) * 0.05;
    camera.lookAt(scene.position);

    for (let i = 0; i < scene.children.length; i++) {
        const object = scene.children[i];
        if (object instanceof THREE.Points) {
            object.rotation.y = Date.now() * 0.00005 * (i % 4 < 2 ? -1 : 1);
        }
    }

    renderer.render(scene, camera);
}

document.addEventListener("DOMContentLoaded", function () {
    const partnerCards = document.querySelectorAll(".partner-card");
    partnerCards.forEach((card, index) => {
        gsap.from(card, {
            duration: 0.6,
            opacity: 0,
            y: 50,
            delay: 0.1 * index,
            ease: "power2.out",
        });
    });
});

const particleContainer = document.getElementById('particleContainer');

for (let i = 0; i < 15; i++) {
    const particle = document.createElement('div');
    particle.classList.add('particle');

    const posX = Math.floor(Math.random() * 100);
    particle.style.left = `${posX}%`;

    const size = Math.random() * 4 + 2;
    particle.style.width = `${size}px`;
    particle.style.height = `${size}px`;

    const duration = Math.random() * 3 + 2;
    const delay = Math.random() * 2;
    particle.style.animationDuration = `${duration}s`;
    particle.style.animationDelay = `${delay}s`;

    particleContainer.appendChild(particle);
}





//
document.addEventListener("DOMContentLoaded", function () {
    const addToCartButtons = document.querySelectorAll(".add-to-cart");

    addToCartButtons.forEach((button) => {
        button.addEventListener("click", () => {
            const productId = parseInt(button.getAttribute("data-id"));
            const existingItem = cart.find(item => item.id === productId);
            if (existingItem) {
                existingItem.quantity += 1;
            } else {
                cart.push({ id: productId, quantity: 1 });
            }
            localStorage.setItem("cart", JSON.stringify(cart));
            console.log(cart);
            sendCartToServer()
        });
    });
});
function sendCartToServer() {
    fetch("/store", {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        body: JSON.stringify(localStorage.getItem("cart"))
    })
        .then(response => {
            if (response.ok) {
                 window.location.reload()
            } else {
                console.error("Gửi giỏ hàng thất bại");
            }
        })
        .catch(error => {
            console.error("Lỗi khi gửi giỏ hàng:", error);
        });
}
