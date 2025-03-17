// Three.js setup
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(
    75,
    window.innerWidth / window.innerHeight,
    0.1,
    1000
);
const renderer = new THREE.WebGLRenderer({
    canvas: document.getElementById("bg-canvas"),
    antialias: true,
    alpha: true,
});

renderer.setSize(window.innerWidth, window.innerHeight);
renderer.setPixelRatio(window.devicePixelRatio);

// Particles
const particlesGeometry = new THREE.BufferGeometry();
const particlesCount = 2000;

const posArray = new Float32Array(particlesCount * 3);
const colorArray = new Float32Array(particlesCount * 3);

for (let i = 0; i < particlesCount * 3; i++) {
    posArray[i] = (Math.random() - 0.5) * 20;

    if (i % 3 === 0) {
        // R component
        colorArray[i] = 0.3 + Math.random() * 0.3; // Blue-ish
    } else if (i % 3 === 1) {
        // G component
        colorArray[i] = 0.5 + Math.random() * 0.3; // Blue-ish
    } else {
        // B component
        colorArray[i] = 0.8 + Math.random() * 0.2; // Blue-ish
    }
}

particlesGeometry.setAttribute(
    "position",
    new THREE.BufferAttribute(posArray, 3)
);
particlesGeometry.setAttribute(
    "color",
    new THREE.BufferAttribute(colorArray, 3)
);

const particlesMaterial = new THREE.PointsMaterial({
    size: 0.02,
    transparent: true,
    opacity: 0.8,
    vertexColors: true,
    blending: THREE.AdditiveBlending,
});

const particlesMesh = new THREE.Points(
    particlesGeometry,
    particlesMaterial
);
scene.add(particlesMesh);

// Light for better visual
const light = new THREE.AmbientLight(0xffffff, 0.5);
scene.add(light);

const pointLight = new THREE.PointLight(0x4e63eb, 2);
pointLight.position.set(0, 0, 2);
scene.add(pointLight);

// Camera position
camera.position.z = 5;

// Mouse effect
let mouseX = 0;
let mouseY = 0;

document.addEventListener("mousemove", (event) => {
    mouseX = (event.clientX / window.innerWidth) * 2 - 1;
    mouseY = -(event.clientY / window.innerHeight) * 2 + 1;
});

// Form interaction - rotate slightly towards mouse
const formContainer = document.getElementById("form-container");

// Animation
const animate = () => {
    requestAnimationFrame(animate);

    // Move particles
    particlesMesh.rotation.x += 0.0005;
    particlesMesh.rotation.y += 0.0007;

    // Subtle movement based on mouse
    particlesMesh.position.x = mouseX * 0.1;
    particlesMesh.position.y = mouseY * 0.1;

    // Form container follows mouse with subtle 3D effect
    if (formContainer) {
        formContainer.style.transform = `perspective(1000px) rotateY(${
            mouseX * 5
        }deg) rotateX(${-mouseY * 5}deg)`;
    }

    renderer.render(scene, camera);
};

animate();

// Handle window resize
window.addEventListener("resize", () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});

// Tab functionality
const tabBtns = document.querySelectorAll(".tab-btn");
const formSections = document.querySelectorAll(".form-section");
const tabLinks = document.querySelectorAll(".tab-link");

function switchTab(tabId) {
    // Update active tab button
    tabBtns.forEach((btn) => {
        if (btn.dataset.tab === tabId) {
            btn.classList.add("active");
        } else {
            btn.classList.remove("active");
        }
    });

    // Show active form section
    formSections.forEach((section) => {
        if (section.id === tabId) {
            section.classList.add("active");
        } else {
            section.classList.remove("active");
        }
    });
}

tabBtns.forEach((btn) => {
    btn.addEventListener("click", () => {
        switchTab(btn.dataset.tab);
    });
});

tabLinks.forEach((link) => {
    link.addEventListener("click", (e) => {
        e.preventDefault();
        switchTab(link.dataset.tab);
    });
});

// // Form validation
// document
//     .getElementById("login-form")
//     .addEventListener("submit", function (e) {
//         e.preventDefault();
//         // Xử lý đăng nhập ở đây
//         console.log("Đăng nhập với:", {
//             email: document.getElementById("login-email").value,
//             password: document.getElementById("login-password").value,
//         });
//     });
//
// document
//     .getElementById("register-form")
//     .addEventListener("submit", function (e) {
//         e.preventDefault();
//         // Kiểm tra mật khẩu xác nhận
//         const password = document.getElementById("register-password").value;
//         const confirm = document.getElementById("register-confirm").value;
//
//         if (password !== confirm) {
//             alert("Mật khẩu xác nhận không khớp!");
//             return;
//         }
//
//         // Xử lý đăng ký ở đây
//         console.log("Đăng ký với:", {
//             name: document.getElementById("register-name").value,
//             email: document.getElementById("register-email").value,
//             password: password,
//         });
//     });
//
