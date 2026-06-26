import os
import re

directory = r"d:\Document\Kuliah\PEMROGRAMAN BERORIENTASI OBJEK\UAS\sigaban_app\src\main\resources\templates"
files = [
    "dashboard.html", "detail_warga.html", "form_warga.html", 
    "bantuan.html", "posko.html", "ai_insight.html", 
    "laporan.html", "pengaturan.html"
]

def get_standard_search(placeholder, needs_outer_wrapper=False):
    ml_class = " ml-md" if needs_outer_wrapper else ""
    # We will just return the inner search box.
    inner = f"""<div class="flex items-center bg-surface-container-low rounded-full px-md py-sm border border-outline-variant focus-within:border-primary transition-colors w-96{ml_class}">
<span class="material-symbols-outlined text-on-surface-variant mr-sm">search</span>
<input class="bg-transparent border-none outline-none font-body-sm text-body-sm text-on-surface w-full focus:ring-0 placeholder:text-outline py-0" placeholder="{placeholder}" type="text"/>
</div>"""
    return inner

# We'll manually specify the regex to replace for each file to be extremely safe, 
# since the wrappers differ wildly.

replacements = {
    "dashboard.html": {
        "pattern": r'<div class="flex items-center bg-surface-container-low rounded-full px-md py-sm border border-outline-variant focus-within:border-primary transition-colors w-96 ml-md">.*?</div>',
        "placeholder": "Search aid, shelters, or records...",
        "outer": True
    },
    "detail_warga.html": {
        "pattern": r'<div class="flex-1 max-w-md mx-lg hidden sm:block">.*?</div>\s*</div>',
        "placeholder": "Search Data Warga...",
        "outer": False
    },
    "form_warga.html": {
        "pattern": r'<!-- Search Bar on_left -->\s*<div class="relative flex items-center">.*?</div>',
        "placeholder": "Cari NIK atau Nama...",
        "outer": False
    },
    "bantuan.html": {
        "pattern": r'<!-- Search Bar -->\s*<div class="flex items-center bg-surface-container-low rounded-full px-md py-sm border border-outline-variant focus-within:border-primary transition-colors w-96">.*?</div>',
        "placeholder": "Search aid, shelters, or records...",
        "outer": False
    },
    "posko.html": {
        "pattern": r'<div class="hidden md:flex items-center bg-surface-container-low rounded-full px-sm py-xs border border-outline-variant focus-within:border-primary transition-colors">.*?</div>',
        "placeholder": "Cari Posko...",
        "outer": False
    },
    "ai_insight.html": {
        "pattern": r'<div class="flex items-center bg-surface-container-low rounded-full px-md py-sm border border-outline-variant focus-within:border-primary transition-colors w-96">.*?</div>',
        "placeholder": "Search models, predictions...",
        "outer": False
    },
    "laporan.html": {
        "pattern": r'<div class="relative flex items-center w-64">.*?</div>',
        "placeholder": "Search reports...",
        "outer": False
    },
    "pengaturan.html": {
        "pattern": r'<div class="relative flex items-center w-64">.*?</div>',
        "placeholder": "Search settings...",
        "outer": False
    }
}

for filename in files:
    filepath = os.path.join(directory, filename)
    if not os.path.exists(filepath): continue
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    rep = replacements.get(filename)
    if not rep: continue
    
    # Actually, using regex with .*? over newlines needs re.DOTALL
    pattern = re.compile(rep["pattern"], re.DOTALL)
    
    # Try to extract the original placeholder if it exists, otherwise use default
    m_placeholder = re.search(r'placeholder="([^"]+)"', pattern.search(content).group(0)) if pattern.search(content) else None
    placeholder = m_placeholder.group(1) if m_placeholder else rep["placeholder"]
    
    new_html = get_standard_search(placeholder, rep["outer"])
    
    if "<!-- Search Bar" in rep["pattern"]:
        # Keep the comment
        comment = re.search(r'(<!--.*?-->)', rep["pattern"]).group(1) if re.search(r'(<!--.*?-->)', rep["pattern"]) else ""
        new_content = pattern.sub(f"{comment}\n{new_html}", content)
    else:
        new_content = pattern.sub(new_html, content)
        
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    print(f"Standardized search box in {filename} (Placeholder: {placeholder})")
