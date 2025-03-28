// ThreeJS animation cho background
const container = document.getElementById("canvas-container");
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(
    75,
    window.innerWidth / window.innerHeight,
    0.1,
    1000
);
const renderer = new THREE.WebGLRenderer({
    antialias: true,
    alpha: true,
});
renderer.setSize(window.innerWidth, window.innerHeight);
renderer.setClearColor(0x000000, 0);
container.appendChild(renderer.domElement);
const particlesGeometry = new THREE.BufferGeometry();
const particlesCount = 1000;

const posArray = new Float32Array(particlesCount * 3);

for (let i = 0; i < particlesCount * 3; i++) {
    posArray[i] = (Math.random() - 0.5) * 8;
}

particlesGeometry.setAttribute(
    "position",
    new THREE.BufferAttribute(posArray, 3)
);

const particlesMaterial = new THREE.PointsMaterial({
    size: 0.02,
    color: 0x6c63ff,
});
const particlesMesh = new THREE.Points(
    particlesGeometry,
    particlesMaterial
);
scene.add(particlesMesh);
camera.position.z = 4;
let mouseX = 0;
let mouseY = 0;

document.addEventListener("mousemove", (event) => {
    mouseX = event.clientX / window.innerWidth - 0.5;
    mouseY = event.clientY / window.innerHeight - 0.5;
});

// Animation
const animate = () => {
    requestAnimationFrame(animate);

    particlesMesh.rotation.y += 0.002;
    particlesMesh.rotation.x += 0.001;
    particlesMesh.rotation.x += mouseY * 0.1;
    particlesMesh.rotation.y += mouseX * 0.1;

    renderer.render(scene, camera);
};

animate();

window.addEventListener("resize", () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});
const observer = new IntersectionObserver(
    (entries) => {
        entries.forEach((entry) => {
            if (entry.isIntersecting) {
                entry.target.classList.add("fade-in");
            }
        });
    },
    { threshold: 0.1 }
);

document
    .querySelectorAll(".feature-card, .gallery-img, h2, .contact-section")
    .forEach((element) => {
        observer.observe(element);
    });

// Smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
        e.preventDefault();

        document.querySelector(this.getAttribute("href")).scrollIntoView({
            behavior: "smooth",
        });
    });
});

// Viết, xóa chữ
document.addEventListener("DOMContentLoaded", function () {
    const titleText = "Hệ thống voucher cho cửa hàng bán lẻ";
    const descText =
        "Mua sắm thả ga, không lo về giá với hàng nghìn voucher giảm giá khác nhau.";

    const typingTitle = document.getElementById("typing-title");
    const typingDesc = document.getElementById("typing-desc");

    typingDesc.textContent = descText;

    let titleIndex = 0;
    let direction = "typing-title";

    const typingSpeed = 20; // Tốc độ viết (ms)
    const deletingSpeed = 20; // Tốc độ xóa (ms)
    const pauseBeforeDelete = 2000; // Dừng trước khi xóa (ms)
    const pauseBeforeType = 100; // Dừng trước khi viết lại (ms)

    function updateAnimation() {
        if (direction === "typing-title") {
            // Đang viết tiêu đề
            titleIndex++;
            typingTitle.textContent = titleText.slice(0, titleIndex);

            if (titleIndex >= titleText.length) {
                // Tiêu đề đã viết xong, chuyển sang chế độ dừng trước khi xóa
                direction = "pausing";
                setTimeout(() => {
                    direction = "deleting-title";
                    updateAnimation();
                }, pauseBeforeDelete);
            } else {
                setTimeout(updateAnimation, typingSpeed);
            }
        } else if (direction === "deleting-title") {
            // Đang xóa tiêu đề
            titleIndex--;
            typingTitle.textContent = titleText.slice(0, titleIndex);

            if (titleIndex <= 0) {
                // Tiêu đề đã xóa xong, chuyển sang chế độ viết lại
                direction = "pausing-before-type";
                setTimeout(() => {
                    direction = "typing-title";
                    updateAnimation();
                }, pauseBeforeType);
            } else {
                setTimeout(updateAnimation, deletingSpeed);
            }
        }
    }

    updateAnimation();
});

// Phần voucher
document.addEventListener("DOMContentLoaded", function () {
    const copyButtons = document.querySelectorAll(".voucher-copy-btn");

    copyButtons.forEach((button) => {
        button.addEventListener("click", function () {
            const code = this.getAttribute("data-code");
            navigator.clipboard.writeText(code).then(() => {
                // Change button text temporarily
                const originalText = this.textContent;
                this.textContent = "Copied!";
                this.classList.add("voucher-copy-btn-copied");

                // Revert back after 2 seconds
                setTimeout(() => {
                    this.textContent = originalText;
                    this.classList.remove("voucher-copy-btn-copied");
                }, 2000);
            });
        });
    });
});

