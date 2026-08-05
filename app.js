/* Pro Bros Restoration — interactions */
(function () {
  // icons
  function icons(){ if (window.lucide) lucide.createIcons(); }
  icons();

  // sticky header shadow
  var header = document.getElementById('header');
  var onScroll = function () {
    if (window.scrollY > 8) header.classList.add('scrolled');
    else header.classList.remove('scrolled');
  };
  window.addEventListener('scroll', onScroll, { passive: true });
  onScroll();

  // mobile drawer
  var drawer = document.getElementById('drawer');
  var burger = document.getElementById('hamburger');
  function openDrawer(){ drawer.classList.add('open'); document.body.style.overflow = 'hidden'; }
  function closeDrawer(){ drawer.classList.remove('open'); document.body.style.overflow = ''; }
  if (burger) burger.addEventListener('click', openDrawer);
  drawer.querySelectorAll('[data-close]').forEach(function (el) {
    el.addEventListener('click', closeDrawer);
  });

  // FAQ accordion
  document.querySelectorAll('.qa').forEach(function (qa) {
    var btn = qa.querySelector('button');
    var ans = qa.querySelector('.ans');
    btn.addEventListener('click', function () {
      var isOpen = qa.classList.contains('open');
      document.querySelectorAll('.qa').forEach(function (other) {
        other.classList.remove('open');
        other.querySelector('.ans').style.maxHeight = null;
      });
      if (!isOpen) {
        qa.classList.add('open');
        ans.style.maxHeight = ans.scrollHeight + 'px';
      }
    });
  });

  // before/after compare slider
  var compare = document.getElementById('compare');
  if (compare) {
    var afterLayer = document.getElementById('afterLayer');
    var handle = document.getElementById('handle');
    var dragging = false;
    function setPos(clientX) {
      var rect = compare.getBoundingClientRect();
      var x = clientX - rect.left;
      var pct = Math.max(4, Math.min(96, (x / rect.width) * 100));
      afterLayer.style.clipPath = 'inset(0 0 0 ' + pct + '%)';
      handle.style.left = pct + '%';
    }
    function start(e){ dragging = true; e.preventDefault(); }
    function move(e){
      if (!dragging) return;
      var cx = e.touches ? e.touches[0].clientX : e.clientX;
      setPos(cx);
    }
    function end(){ dragging = false; }
    compare.addEventListener('mousedown', function(e){ start(e); setPos(e.clientX); });
    compare.addEventListener('touchstart', function(e){ start(e); setPos(e.touches[0].clientX); }, { passive: false });
    window.addEventListener('mousemove', move);
    window.addEventListener('touchmove', move, { passive: false });
    window.addEventListener('mouseup', end);
    window.addEventListener('touchend', end);
  }

  // lead form
  var form = document.getElementById('leadForm');
  if (form) {
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      var name = form.querySelector('#name');
      var phone = form.querySelector('#phone');
      var ok = true;
      [name, phone].forEach(function (f) {
        if (!f.value.trim()) { f.style.borderColor = 'var(--alert)'; ok = false; }
        else { f.style.borderColor = ''; }
      });
      if (!ok) return;
      form.style.display = 'none';
      document.getElementById('formSuccess').classList.add('show');
    });
    ['#name', '#phone'].forEach(function (sel) {
      var f = form.querySelector(sel);
      f.addEventListener('input', function () { f.style.borderColor = ''; });
    });
  }

  // reveal on scroll
  var io = new IntersectionObserver(function (entries) {
    entries.forEach(function (en) {
      if (en.isIntersecting) { en.target.classList.add('in'); io.unobserve(en.target); }
    });
  }, { threshold: 0.12, rootMargin: '0px 0px -40px 0px' });
  document.querySelectorAll('.reveal').forEach(function (el) { io.observe(el); });

  // smooth-close drawer on hash nav handled by data-close; re-run icons after lucide load
  window.addEventListener('load', icons);
})();
