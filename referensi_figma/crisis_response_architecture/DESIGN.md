---
name: Crisis Response Architecture
colors:
  surface: '#f8f9fa'
  surface-dim: '#d9dadb'
  surface-bright: '#f8f9fa'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f4f5'
  surface-container: '#edeeef'
  surface-container-high: '#e7e8e9'
  surface-container-highest: '#e1e3e4'
  on-surface: '#191c1d'
  on-surface-variant: '#424752'
  inverse-surface: '#2e3132'
  inverse-on-surface: '#f0f1f2'
  outline: '#727784'
  outline-variant: '#c2c6d4'
  surface-tint: '#115cb9'
  primary: '#003f87'
  on-primary: '#ffffff'
  primary-container: '#0056b3'
  on-primary-container: '#bbd0ff'
  inverse-primary: '#acc7ff'
  secondary: '#984700'
  on-secondary: '#ffffff'
  secondary-container: '#ff8016'
  on-secondary-container: '#5f2a00'
  tertiary: '#004c17'
  on-tertiary: '#ffffff'
  tertiary-container: '#006722'
  on-tertiary-container: '#6fe87c'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d7e2ff'
  primary-fixed-dim: '#acc7ff'
  on-primary-fixed: '#001a40'
  on-primary-fixed-variant: '#004491'
  secondary-fixed: '#ffdbc8'
  secondary-fixed-dim: '#ffb68a'
  on-secondary-fixed: '#321300'
  on-secondary-fixed-variant: '#743500'
  tertiary-fixed: '#83fc8e'
  tertiary-fixed-dim: '#66df75'
  on-tertiary-fixed: '#002106'
  on-tertiary-fixed-variant: '#00531a'
  background: '#f8f9fa'
  on-background: '#191c1d'
  surface-variant: '#e1e3e4'
typography:
  headline-xl:
    fontFamily: Inter
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  headline-sm:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  gutter: 16px
  margin-mobile: 16px
  margin-desktop: 32px
---

## Brand & Style

The design system is engineered for **SIGABAN**, focusing on high-stakes disaster management where clarity, speed, and reliability are paramount. The brand personality is **authoritative yet calm**, providing a "steady hand" during chaotic flood events. 

The visual style blends **Corporate Modern** with subtle **Glassmorphic** accents for AI-driven insights. It prioritizes a high information density without sacrificing legibility. The UI should evoke a sense of organized urgency—users must feel that the data is live, accurate, and actionable. We utilize white space strategically to separate critical alerts from general administrative data, ensuring the "signal-to-noise" ratio remains optimal for emergency responders.

## Colors

The palette is rooted in professional stability and immediate recognition. 

- **Primary (Professional Blue):** Used for navigation, primary actions, and branding. It establishes trust and institutional authority.
- **Backgrounds:** A mix of Pure White (#FFFFFF) for main content areas and Light Grey (#F8F9FA) for sidebars and grouping containers to provide subtle depth.
- **Status Colors:**
    - **Emergency Red (#DC3545):** Reserved strictly for critical flood levels, immediate danger alerts, and high-priority evacuations.
    - **Warning Orange (#FD7E14):** Used for rising water levels or "standby" status notifications.
    - **Success Green (#28A745):** Indicates safe zones, completed evacuations, or stabilized water levels.
- **AI Tints:** A very soft, glowing blue tint (#E7F0FF) is used behind AI-driven prediction cards to differentiate them from standard historical data.

## Typography

This design system utilizes **Inter** for its exceptional legibility in data-heavy environments. 

- **Weight Usage:** Use **Bold (700)** for primary headlines and **SemiBold (600)** for sub-headers and labels. **Regular (400)** is the standard for all body copy and descriptions.
- **Data Display:** For numerical values in tables or dashboards (e.g., water levels in meters), use `label-md` with a slightly tighter letter spacing to ensure readability at a glance.
- **Responsive Scale:** Headlines scale down significantly for mobile devices to ensure dashboard widgets remain functional on smaller screens used by field officers.

## Layout & Spacing

The design system follows a **12-column fluid grid** for desktop dashboards, transitioning to a single-column layout for mobile field reports.

- **Grid Logic:** Use a 16px gutter on desktop to maximize screen real estate while maintaining separation. 
- **The 8px Rhythm:** All padding, margins, and component heights must be multiples of 8px. 
- **Content Grouping:** Use `lg` (24px) spacing between disparate data widgets and `md` (16px) spacing for elements within a single card or container.
- **Mobile Adaption:** Sidebars collapse into a bottom navigation bar or a hamburger menu on screens smaller than 768px. Dashboards should reflow from a 3-card-per-row layout to a single vertical stack.

## Elevation & Depth

To maintain a clean and professional look, depth is used sparingly to signify interactivity and hierarchy:

- **Flat Layer (Level 0):** Background surfaces (#F8F9FA).
- **Surface Layer (Level 1):** Main content cards and navigation bars. These use a very subtle 1px border (#E9ECEF) instead of heavy shadows.
- **Raised Layer (Level 2):** Hover states and active dropdowns. These use an ambient shadow: `0px 4px 12px rgba(0, 0, 0, 0.05)`.
- **Emergency Overlay (Level 3):** Modals and critical alerts. These use a more pronounced shadow: `0px 12px 32px rgba(0, 0, 0, 0.12)` and a soft backdrop blur (8px) to focus the user's attention.
- **AI Elevation:** Cards containing predictive analytics feature a soft, inner blue glow (2px spread) to visually distinguish "forecasted" data from "observed" data.

## Shapes

The shape language is modern and approachable. 

- **Containers:** Main dashboard widgets and cards use `rounded-lg` (16px) to soften the "technical" feel of the data.
- **Interactive Elements:** Buttons and input fields use `rounded` (8px) for a precise, professional appearance.
- **Status Badges:** Use fully rounded (pill-shaped) geometry to distinguish them from interactive buttons.
- **Data Tables:** Tables remain sharp on the outer corners if nested within a card, but the containing card provides the 16px radius.

## Components

### Buttons
- **Primary:** Solid #0056B3 with white text. 8px border radius.
- **Secondary:** Transparent with #0056B3 border and text.
- **Danger:** Solid #DC3545 for destructive or emergency actions (e.g., "Issue Alert").

### Status Badges
- Used for "Safe," "At Risk," and "Evacuated." 
- High-contrast text on low-opacity backgrounds (e.g., Dark Green text on 15% opacity Green background) to ensure accessibility without overwhelming the visual field.

### AI Prediction Cards
- These include a small Sparkline graph and a subtle gradient background (White to #E7F0FF).
- Must include an "AI Insight" icon (e.g., a sparkle or brain icon) in the top right corner.

### Data Tables
- Clean, no vertical borders. 
- Alternating row stripes are not used; instead, use a 1px bottom border (#F1F3F5) between rows.
- Header row must be `label-sm` with a light grey background.

### Input Fields
- White background with a 1px border (#DEE2E6).
- Focus state: 2px border #0056B3 with a soft blue outer glow.

### Map Interface
- Map controls should be floating, glassmorphic elements with a `backdrop-filter: blur(10px)`.
- Markers for flood areas should use pulsated animations if the water level is rising.