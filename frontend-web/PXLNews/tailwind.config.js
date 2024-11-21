/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/**/*.{html,ts}",
    ],
    theme: {
        colors: {
            'white': '#FFFFFF', // Wit
            'primary': '#a1d5f0',  // Hoofdkleur: Lichtblauw
            'secondary': '#72c1ed', // Secundaire kleur: Hemelsblauw
            'button-primary': '#3498db', // Primaire knop kleur: Blauw
            'secondary-light': '#3498db', // Lichte secundaire kleur
            'light-background': '#87CEEB', // Lichte achtergrondkleur
            'dark-background': '#34495e', // Donkere achtergrondkleur
            'accent': '#e67e22', // Accent kleur: Oranje
            'highlight': '#f39c12', // Gouden geel voor belangrijke secties
            'text-primary': '#2c3e50', // Primaire tekstkleur
            'text-secondary': '#FFFFFF', // Secundaire tekstkleur
            'error': '#e74c3c', // Fouten kleur (rood)
            'warning': '#f39c12', // Waarschuwing kleur (oranje)
            'success': '#27ae60', // Succes kleur (groen)
            'neutral-dark': '#7f8c8d', // Neutrale donkere tint
            'neutral-light': '#bdc3c7', // Neutrale lichte tint
        },
        fontFamily: {
            body: ['Nunito', 'sans-serif'],
        },
    },
    plugins: [],
}
