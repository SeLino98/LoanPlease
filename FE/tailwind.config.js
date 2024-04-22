/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx,ts,tsx}"
  ],
  theme: {
    extend: {
      fontFamily: {
        cusFont1: ['비트비트체v2'], // 비트비트체v2
        cusFont2: ['Orbit'], // Orbit
      }
    },
  },
  plugins: [],
}

