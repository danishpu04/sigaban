import os
import re

filepath = r"d:\Document\Kuliah\PEMROGRAMAN BERORIENTASI OBJEK\UAS\sigaban_app\src\main\resources\templates\posko.html"

routes = {
    "Dashboard": "/dashboard",
    "Data Warga": "/data-warga",
    "Bantuan": "/bantuan",
    "Posko": "/posko",
    "AI Insight": "/ai-insight",
    "Reports": "/laporan",
    "Settings": "/pengaturan"
}

with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()
    
# In posko.html, the structure is:
# href="#">
# <span class="material-symbols-outlined" ...>...</span>
# Dashboard
# </a>

# Let's replace href="#" with the correct route based on the text.
for label, route in routes.items():
    # We look for href="#" followed by anything up to the label, but we want to make sure we only replace the closest href="#"
    # So we match href="#" then characters (not containing href="#") until the label.
    # Actually, a simpler way is just to find the label, go backwards to the nearest href="#", and replace it.
    
    parts = content.split(label)
    if len(parts) > 1:
        for i in range(len(parts) - 1):
            part = parts[i]
            # check if this is inside the nav menu. We can just replace the LAST href="#" in `part`
            last_href_idx = part.rfind('href="#"')
            if last_href_idx != -1:
                # Replace just that one
                parts[i] = part[:last_href_idx] + f'href="{route}"' + part[last_href_idx+8:]
        
        content = label.join(parts)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)
print("Fixed posko.html")
