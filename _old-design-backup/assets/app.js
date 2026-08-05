// Pro Bros Restoration — site interactions
(function () {
  // Mobile nav toggle
  document.addEventListener('click', (e) => {
    const btn = e.target.closest('[data-nav-toggle]');
    if (btn) {
      const links = document.querySelector('.nav-links');
      if (links) links.classList.toggle('open');
    }
    const navlink = e.target.closest('.nav-links a');
    if (navlink) {
      const links = document.querySelector('.nav-links');
      if (links) links.classList.remove('open');
    }
  });

  // Scroll reveal
  const io = new IntersectionObserver((entries) => {
    entries.forEach(en => {
      if (en.isIntersecting) {
        en.target.classList.add('in');
        io.unobserve(en.target);
      }
    });
  }, { threshold: 0.12, rootMargin: '0px 0px -40px 0px' });
  document.querySelectorAll('.reveal').forEach(el => io.observe(el));

  // Before/after slider
  document.querySelectorAll('[data-ba]').forEach(setupBA);

  function setupBA(el) {
    const handle = el.querySelector('.ba-handle');
    const after = el.querySelector('.ba-after');
    let dragging = false;

    const setPos = (clientX) => {
      const r = el.getBoundingClientRect();
      const x = Math.min(Math.max(clientX - r.left, 0), r.width);
      const pct = (x / r.width) * 100;
      after.style.clipPath = `inset(0 ${100 - pct}% 0 0)`;
      handle.style.left = pct + '%';
    };

    const start = (e) => { dragging = true; el.classList.add('ba-active'); move(e); };
    const move = (e) => {
      if (!dragging) return;
      const t = e.touches ? e.touches[0] : e;
      setPos(t.clientX);
    };
    const end = () => { dragging = false; el.classList.remove('ba-active'); };

    handle.addEventListener('mousedown', start);
    handle.addEventListener('touchstart', start, { passive: true });
    window.addEventListener('mousemove', move);
    window.addEventListener('touchmove', move, { passive: true });
    window.addEventListener('mouseup', end);
    window.addEventListener('touchend', end);

    // click anywhere to jump
    el.addEventListener('click', (e) => {
      if (e.target.closest('.ba-handle')) return;
      const r = el.getBoundingClientRect();
      setPos(e.clientX);
    });

    // initialize at 50%
    requestAnimationFrame(() => {
      const r = el.getBoundingClientRect();
      setPos(r.left + r.width / 2);
    });
  }

  // Contact form (demo handler)
  const form = document.querySelector('[data-contact-form]');
  if (form) {
    form.addEventListener('submit', (e) => {
      e.preventDefault();
      const status = form.querySelector('[data-form-status]');
      const btn = form.querySelector('button[type=submit]');
      const data = new FormData(form);
      const name = (data.get('name') || '').toString().trim();
      const email = (data.get('email') || '').toString().trim();
      if (!name || !email) {
        status.textContent = 'Please add your name and email so we can reach you.';
        status.dataset.tone = 'warn';
        return;
      }
      btn.disabled = true;
      btn.textContent = 'Sending…';
      setTimeout(() => {
        status.textContent = `Thanks ${name.split(' ')[0]} — we'll be in touch within 30 minutes. For emergencies call (469) 585-5519.`;
        status.dataset.tone = 'ok';
        form.reset();
        btn.disabled = false;
        btn.textContent = 'Send message';
      }, 800);
    });
  }

  // Service tab switcher (services page)
  document.querySelectorAll('[data-service-tabs]').forEach(group => {
    const tabs = group.querySelectorAll('[data-svc-tab]');
    const panels = group.querySelectorAll('[data-svc-panel]');
    tabs.forEach(t => t.addEventListener('click', () => {
      const id = t.dataset.svcTab;
      tabs.forEach(x => x.classList.toggle('active', x === t));
      panels.forEach(p => p.classList.toggle('active', p.dataset.svcPanel === id));
    }));
  });

  // Portfolio filter
  document.querySelectorAll('[data-filter-group]').forEach(group => {
    const btns = group.querySelectorAll('[data-filter]');
    btns.forEach(b => b.addEventListener('click', () => {
      const f = b.dataset.filter;
      btns.forEach(x => x.classList.toggle('active', x === b));
      document.querySelectorAll('[data-tag]').forEach(item => {
        const tags = item.dataset.tag.split(' ');
        item.style.display = (f === 'all' || tags.includes(f)) ? '' : 'none';
      });
    }));
  });
})();
