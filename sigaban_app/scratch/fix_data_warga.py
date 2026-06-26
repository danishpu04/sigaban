import os
import re

directory = r"d:\Document\Kuliah\PEMROGRAMAN BERORIENTASI OBJEK\UAS\sigaban_app\src\main\resources\templates"
files_to_update = {
    "detail_warga.html": "/data-warga",
    "form_warga.html": "/data-warga"
}

standard_sidebar = """<!-- SideNavBar -->
<nav class="hidden md:flex h-screen w-64 fixed left-0 top-0 bg-surface-container border-r border-outline-variant flex flex-col h-full py-md z-50">
<div class="px-md pb-lg">
<h1 class="font-headline-sm text-headline-sm font-bold text-primary">SIGABAN</h1>
<p class="font-label-sm text-label-sm text-on-surface-variant">Disaster Mgmt System</p>
</div>
<ul class="flex flex-col flex-grow w-full space-y-1">
<li>
<a class="flex items-center px-md py-sm text-on-surface-variant hover:bg-surface-container-highest transition-colors" href="/dashboard">
<span class="material-symbols-outlined mr-md">dashboard</span>
<span class="font-label-md text-label-md">Dashboard</span>
</a>
</li>
<li>
<a class="flex items-center px-md py-sm text-on-surface-variant hover:bg-surface-container-highest transition-colors" href="/data-warga">
<span class="material-symbols-outlined mr-md">groups</span>
<span class="font-label-md text-label-md">Data Warga</span>
</a>
</li>
<li>
<a class="flex items-center px-md py-sm text-on-surface-variant hover:bg-surface-container-highest transition-colors" href="/bantuan">
<span class="material-symbols-outlined mr-md">handshake</span>
<span class="font-label-md text-label-md">Bantuan</span>
</a>
</li>
<li>
<a class="flex items-center px-md py-sm text-on-surface-variant hover:bg-surface-container-highest transition-colors" href="/posko">
<span class="material-symbols-outlined mr-md">location_on</span>
<span class="font-label-md text-label-md">Posko</span>
</a>
</li>
<li>
<a class="flex items-center px-md py-sm text-on-surface-variant hover:bg-surface-container-highest transition-colors" href="/ai-insight">
<span class="material-symbols-outlined mr-md">psychology</span>
<span class="font-label-md text-label-md">AI Insight</span>
</a>
</li>
<li>
<a class="flex items-center px-md py-sm text-on-surface-variant hover:bg-surface-container-highest transition-colors" href="/laporan">
<span class="material-symbols-outlined mr-md">assessment</span>
<span class="font-label-md text-label-md">Reports</span>
</a>
</li>
<li class="mt-auto pt-lg border-t border-outline-variant">
<a class="flex items-center px-md py-sm text-on-surface-variant hover:bg-surface-container-highest transition-colors" href="/pengaturan">
<span class="material-symbols-outlined mr-md">settings</span>
<span class="font-label-md text-label-md">Settings</span>
</a>
</li>
</ul>
</nav>"""

active_class = 'class="flex items-center px-md py-sm text-primary font-bold border-r-4 border-primary bg-primary-container/10 transition-opacity duration-200 opacity-80"'
inactive_class = 'class="flex items-center px-md py-sm text-on-surface-variant hover:bg-surface-container-highest transition-colors"'

for filename, active_route in files_to_update.items():
    filepath = os.path.join(directory, filename)
    if not os.path.exists(filepath): continue
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    if content.startswith("!DOCTYPE"):
        content = "<" + content
        
    # Pattern to match <aside> or <nav> after <!-- SideNavBar -->
    pattern = re.compile(r'<!-- SideNavBar -->\s*<(nav|aside)[^>]*>.*?</\1>', re.DOTALL)
    
    custom_sidebar = standard_sidebar
    target_link_pattern = re.compile(r'<a ' + re.escape(inactive_class) + r' (href="' + re.escape(active_route) + r'")>')
    custom_sidebar = target_link_pattern.sub(f'<a {active_class} \\1>', custom_sidebar)
    
    new_content = pattern.sub(custom_sidebar, content)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_content)
        
    print(f"Fixed sidebar in {filename}")
