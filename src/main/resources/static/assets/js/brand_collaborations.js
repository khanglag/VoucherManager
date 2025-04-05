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
<<<<<<< HEAD
});
=======
});


document.addEventListener("DOMContentLoaded", function() {
    const particlesContainer = document.getElementById('particles');
    const svg = document.querySelector('.connect-lines svg');
    const lines = [];
    const particles = [];
    const maxParticles = 40;
    const maxLines = 15;

    // Create particles
    for (let i = 0; i < maxParticles; i++) {
        createParticle();
    }

    function createParticle() {
        const particle = document.createElement('div');
        particle.classList.add('particle');

        // Random starting position
        const posX = Math.random() * 100;
        const posY = Math.random() * 110 + 100; // Start from below

        // Random size
        const size = Math.random() * 4 + 2;

        // Random animation duration and delay
        const duration = Math.random() * 10 + 15;
        const delay = Math.random() * 10;

        // Random color from blue/purple palette
        const colors = [
            'rgba(59, 130, 246, 0.7)', // blue
            'rgba(139, 92, 246, 0.7)',  // purple
            'rgba(236, 72, 153, 0.7)',  // pink
            'rgba(248, 113, 113, 0.7)', // red
            'rgba(245, 158, 11, 0.7)'   // amber
        ];
        const color = colors[Math.floor(Math.random() * colors.length)];

        particle.style.left = `${posX}%`;
        particle.style.top = `${posY}%`;
        particle.style.width = `${size}px`;
        particle.style.height = `${size}px`;
        particle.style.backgroundColor = color;
        particle.style.boxShadow = `0 0 ${size * 2}px ${color}`;
        particle.style.animationDuration = `${duration}s`;
        particle.style.animationDelay = `${delay}s`;

        particlesContainer.appendChild(particle);
        particles.push({
            element: particle,
            x: posX,
            y: posY
        });

        // Remove and recreate particle when animation ends
        setTimeout(() => {
            particles.splice(particles.indexOf(particle), 1);
            particle.remove();
            createParticle();
        }, (duration + delay) * 1000);
    }

    // Create connecting lines animation
    function createConnectingLines() {
        // Clear existing lines
        while (svg.firstChild) {
            svg.removeChild(svg.firstChild);
        }

        // Create random connecting lines
        for (let i = 0; i < maxLines; i++) {
            const line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
            line.setAttribute('class', 'line');

            const x1 = Math.random() * 100;
            const y1 = Math.random() * 100;
            const x2 = Math.random() * 100;
            const y2 = Math.random() * 100;

            line.setAttribute('x1', `${x1}%`);
            line.setAttribute('y1', `${y1}%`);
            line.setAttribute('x2', `${x2}%`);
            line.setAttribute('y2', `${y2}%`);

            const opacity = Math.random() * 0.15 + 0.05;
            line.style.stroke = `rgba(255, 255, 255, ${opacity})`;

            svg.appendChild(line);
            lines.push(line);
        }
    }

    // Animate the lines
    function animateLines() {
        lines.forEach(line => {
            const x1Change = Math.random() * 2 - 1; // -1 to 1
            const y1Change = Math.random() * 2 - 1;
            const x2Change = Math.random() * 2 - 1;
            const y2Change = Math.random() * 2 - 1;

            let x1 = parseFloat(line.getAttribute('x1'));
            let y1 = parseFloat(line.getAttribute('y1'));
            let x2 = parseFloat(line.getAttribute('x2'));
            let y2 = parseFloat(line.getAttribute('y2'));

            x1 += x1Change;
            y1 += y1Change;
            x2 += x2Change;
            y2 += y2Change;

            // Keep within bounds
            x1 = Math.min(Math.max(x1, 0), 100);
            y1 = Math.min(Math.max(y1, 0), 100);
            x2 = Math.min(Math.max(x2, 0), 100);
            y2 = Math.min(Math.max(y2, 0), 100);

            line.setAttribute('x1', `${x1}%`);
            line.setAttribute('y1', `${y1}%`);
            line.setAttribute('x2', `${x2}%`);
            line.setAttribute('y2', `${y2}%`);
        });

        requestAnimationFrame(animateLines);
    }

    // Initialize connecting lines
    createConnectingLines();
    requestAnimationFrame(animateLines);

    // Periodically recreate lines for variety
    setInterval(createConnectingLines, 10000);
});
>>>>>>> main
