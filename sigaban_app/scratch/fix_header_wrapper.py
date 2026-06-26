import os
import re

directory = r"d:\Document\Kuliah\PEMROGRAMAN BERORIENTASI OBJEK\UAS\sigaban_app\src\main\resources\templates"
files = [
    "dashboard.html", "detail_warga.html", "form_warga.html", 
    "bantuan.html", "posko.html", "ai_insight.html", 
    "laporan.html", "pengaturan.html"
]

for filename in files:
    filepath = os.path.join(directory, filename)
    if not os.path.exists(filepath): continue
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    # We want to standardize the ENTIRE <header> block.
    # But wait, we need to extract the placeholder.
    m_placeholder = re.search(r'placeholder="([^"]+)"', content)
    # Actually, we know the placeholders.
    # But wait, extracting the placeholder inside the <header> is safer.
    header_match = re.search(r'<header[^>]*>.*?</header>', content, re.DOTALL)
    if not header_match: continue
    header_content = header_match.group(0)
    m_placeholder = re.search(r'placeholder="([^"]+)"', header_content)
    placeholder = m_placeholder.group(1) if m_placeholder else "Search..."
    
    # We will keep the original <header> tag because it might have ml-64 (like dashboard)
    header_tag_match = re.search(r'<header[^>]*>', header_content)
    header_tag = header_tag_match.group(0)
    
    # Check if this header has a hamburger menu button (like detail_warga.html)
    # Some pages have a hamburger menu button for mobile inside the header.
    # dashboard.html does not have it inside the header.
    
    standard_header_inner = f"""
<div class="flex items-center gap-md">
    <div class="flex items-center bg-surface-container-low rounded-full px-md py-sm border border-outline-variant focus-within:border-primary transition-colors w-96">
        <span class="material-symbols-outlined text-on-surface-variant mr-sm">search</span>
        <input class="bg-transparent border-none outline-none font-body-sm text-body-sm text-on-surface w-full focus:ring-0 placeholder:text-outline py-0" placeholder="{placeholder}" type="text"/>
    </div>
</div>
<div class="flex items-center gap-md">
    <button class="text-on-surface-variant hover:text-primary transition-colors">
        <span class="material-symbols-outlined">notifications</span>
    </button>
    <button class="text-on-surface-variant hover:text-primary transition-colors flex items-center gap-sm">
        <span class="material-symbols-outlined">account_circle</span>
    </button>
</div>
"""
    # Wait, dashboard.html had ml-md on the search box: class="... w-96 ml-md"
    if "dashboard.html" in filename:
        standard_header_inner = standard_header_inner.replace('w-96"', 'w-96 ml-md"')
    
    new_header = header_tag + standard_header_inner + "</header>"
    
    # Wait, `form_warga.html` and `detail_warga.html` have a hamburger menu button:
    # <button class="md:hidden text-on-surface-variant hover:text-primary transition-colors">
    # <span class="material-symbols-outlined">menu</span>
    # </button>
    # If we replace the whole header inner, we lose the hamburger menu button. But wait! dashboard.html doesn't have it either. Does the user care?
    # If we want to keep it, we can prepend it to the first flex container.
    hamburger = ""
    if "menu" in header_content and "md:hidden" in header_content:
        hamburger = """<button class="md:hidden text-on-surface-variant hover:text-primary transition-colors mr-sm">
        <span class="material-symbols-outlined">menu</span>
    </button>
    """
    
    standard_header_inner_with_burger = f"""
<div class="flex items-center">
    {hamburger}<div class="flex items-center bg-surface-container-low rounded-full px-md py-sm border border-outline-variant focus-within:border-primary transition-colors w-96">
        <span class="material-symbols-outlined text-on-surface-variant mr-sm">search</span>
        <input class="bg-transparent border-none outline-none font-body-sm text-body-sm text-on-surface w-full focus:ring-0 placeholder:text-outline py-0" placeholder="{placeholder}" type="text"/>
    </div>
</div>
<div class="flex items-center gap-md">
    <button class="text-on-surface-variant hover:text-primary transition-colors">
        <span class="material-symbols-outlined">notifications</span>
    </button>
    <button class="text-on-surface-variant hover:text-primary transition-colors flex items-center gap-sm">
        <span class="material-symbols-outlined">account_circle</span>
    </button>
</div>
"""
    new_header = header_tag + standard_header_inner_with_burger + "</header>"
    
    new_content = content.replace(header_content, new_header)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_content)
        
    print(f"Fixed header wrapper in {filename}")
