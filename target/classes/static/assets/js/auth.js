<<<<<<< HEAD
// Three.js setup
=======
// Three.js
>>>>>>> main
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

<<<<<<< HEAD
// Particles
=======
>>>>>>> main
const particlesGeometry = new THREE.BufferGeometry();
const particlesCount = 2000;

const posArray = new Float32Array(particlesCount * 3);
const colorArray = new Float32Array(particlesCount * 3);

for (let i = 0; i < particlesCount * 3; i++) {
    posArray[i] = (Math.random() - 0.5) * 20;

    if (i % 3 === 0) {
        // R component
<<<<<<< HEAD
        colorArray[i] = 0.3 + Math.random() * 0.3; // Blue-ish
    } else if (i % 3 === 1) {
        // G component
        colorArray[i] = 0.5 + Math.random() * 0.3; // Blue-ish
    } else {
        // B component
        colorArray[i] = 0.8 + Math.random() * 0.2; // Blue-ish
=======
        colorArray[i] = 0.3 + Math.random() * 0.3;
    } else if (i % 3 === 1) {
        // G component
        colorArray[i] = 0.5 + Math.random() * 0.3;
    } else {
        // B component
        colorArray[i] = 0.8 + Math.random() * 0.2;
>>>>>>> main
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

<<<<<<< HEAD
// Light for better visual
=======
>>>>>>> main
const light = new THREE.AmbientLight(0xffffff, 0.5);
scene.add(light);

const pointLight = new THREE.PointLight(0x4e63eb, 2);
pointLight.position.set(0, 0, 2);
scene.add(pointLight);

<<<<<<< HEAD
// Camera position
camera.position.z = 5;

// Mouse effect
=======
camera.position.z = 5;

>>>>>>> main
let mouseX = 0;
let mouseY = 0;

document.addEventListener("mousemove", (event) => {
    mouseX = (event.clientX / window.innerWidth) * 2 - 1;
    mouseY = -(event.clientY / window.innerHeight) * 2 + 1;
});

<<<<<<< HEAD
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
=======
const formContainer = document.getElementById("form-container");

const animate = () => {
    requestAnimationFrame(animate);

    particlesMesh.rotation.x += 0.0005;
    particlesMesh.rotation.y += 0.0007;

    particlesMesh.position.x = mouseX * 0.1;
    particlesMesh.position.y = mouseY * 0.1;

>>>>>>> main
    if (formContainer) {
        formContainer.style.transform = `perspective(1000px) rotateY(${
            mouseX * 5
        }deg) rotateX(${-mouseY * 5}deg)`;
    }

    renderer.render(scene, camera);
};

animate();

<<<<<<< HEAD
// Handle window resize
=======
>>>>>>> main
window.addEventListener("resize", () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
});

<<<<<<< HEAD
// Tab functionality
=======
>>>>>>> main
const tabBtns = document.querySelectorAll(".tab-btn");
const formSections = document.querySelectorAll(".form-section");
const tabLinks = document.querySelectorAll(".tab-link");

function switchTab(tabId) {
<<<<<<< HEAD
    // Update active tab button
=======
>>>>>>> main
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

<<<<<<< HEAD
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
=======
const btnSubmit = document.getElementById("btnSubmit");
btnSubmit.addEventListener("click", (event) => {
    event.preventDefault();

    const fullName = document.getElementById("fullName");
    const email = document.getElementById("email");
    const username = document.getElementById("register-username");
    const password = document.getElementById("pass");
    const rePass = document.getElementById("repass");
    const phoneNumber = document.getElementById("phoneNumber");

    const jsonData = JSON.stringify({
        username: username.value.trim(),
        fullName: fullName.value.trim(),
        password: password.value.trim(),
        email: email.value.trim(),
        phoneNumber: phoneNumber.value.trim(),
        status: false
    });
    if (password.value.length < 8) {
        showAlert("⚠️ Vui lòng đặt mật khẩu trên 8 ký tự", "error");
        return;
    }
    if (!fullName.value || !email.value || !username.value || !password.value || !rePass.value || !phoneNumber.value) {
        showAlert("⚠️ Vui lòng nhập đầy đủ thông tin!", "error");
        return;
    }

    if (password.value !== rePass.value) {
        showAlert("⚠️ Mật khẩu không khớp!", "error");
        return;
    }

    fetch("/auth/register", {
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        body: jsonData
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                showAlert(`❌ ${data.error}`, "error");
            } else if (data.message) {
                showAlert("🎉 Đăng ký thành công!", "success");
                fullName.value = "";
                email.value = "";
                username.value = "";
                password.value = "";
                rePass.value = "";
                phoneNumber.value = "";
            }
        })
        .catch(error => {
            console.error("Lỗi:", error);
            showAlert("⚠️ Lỗi hệ thống! Vui lòng thử lại.", "error");
        });
});

function showAlert(message, type) {
    const alertBox = document.getElementById("alert-box");
    const alertMessage = document.getElementById("alert-message");

    alertMessage.textContent = message;
    alertBox.className = `alert show ${type}`;

    setTimeout(() => {
        alertBox.classList.remove("show");
    }, 2000);
}
>>>>>>> main
