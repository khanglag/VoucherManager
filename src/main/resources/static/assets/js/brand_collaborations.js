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

    // Particle geometry
    geometry = new THREE.BufferGeometry();
    const vertices = [];
    const textureLoader = new THREE.TextureLoader();

    // Create particles
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

    // Materials
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

    // Setup renderer
    renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setPixelRatio(window.devicePixelRatio);
    renderer.setSize(window.innerWidth, window.innerHeight);
    document
        .getElementById("canvas-container")
        .appendChild(renderer.domElement);

    // Event listeners
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

// Animation for partner cards
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