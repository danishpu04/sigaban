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
        
    header_match = re.search(r'<header([^>]*)>', content)
    if not header_match: continue
    
    header_tag = header_match.group(0)
    classes_match = re.search(r'class="([^"]+)"', header_tag)
    
    if classes_match:
        classes = classes_match.group(1)
        if "justify-between" not in classes:
            new_classes = classes + " flex justify-between items-center w-full px-lg py-sm h-16"
            new_header_tag = header_tag.replace(f'class="{classes}"', f'class="{new_classes}"')
            content = content.replace(header_tag, new_header_tag)
            
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Fixed header flex classes in {filename}")
