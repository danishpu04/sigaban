import os

directory = r"d:\Document\Kuliah\PEMROGRAMAN BERORIENTASI OBJEK\UAS\sigaban_app\src\main\resources\templates"
files = [
    "dashboard.html", "detail_warga.html", "form_warga.html", 
    "bantuan.html", "posko.html", "ai_insight.html", 
    "laporan.html", "pengaturan.html"
]

target = '<button class="text-on-surface-variant hover:text-primary transition-colors flex items-center gap-sm">'
replacement = '<button class="text-on-surface-variant hover:text-primary transition-colors">'

for filename in files:
    filepath = os.path.join(directory, filename)
    if not os.path.exists(filepath): continue
    
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    if target in content:
        new_content = content.replace(target, replacement)
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Fixed button alignment in {filename}")
