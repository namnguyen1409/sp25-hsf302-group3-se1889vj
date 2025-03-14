(() => {
    'use strict'

    const getStoredTheme = () => localStorage.getItem('theme')
    const setStoredTheme = theme => localStorage.setItem('theme', theme)

    const getPreferredTheme = () => {
        const storedTheme = getStoredTheme()
        if (storedTheme) {
            return storedTheme
        }
        return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
    }

    const setTheme = theme => {
        const isDarkMode = theme === 'auto'
            ? window.matchMedia('(prefers-color-scheme: dark)').matches
            : theme === 'dark';

        document.documentElement.setAttribute('data-bs-theme', isDarkMode ? 'dark' : 'light')
    }

    setTheme(getPreferredTheme())

    const showActiveTheme = (theme, focus = false) => {
        const themeSwitcher = document.querySelector('#bd-theme')
        if (!themeSwitcher) return

        const themeSwitcherText = document.querySelector('#bd-theme-text')
        const activeThemeIcon = document.querySelector('.theme-icon-active')
        if (!activeThemeIcon) {
            console.warn("Không tìm thấy .theme-icon-active")
            return
        }

        const btnToActive = document.querySelector(`[data-bs-theme-value="${theme}"]`)
        if (!btnToActive) {
            console.warn(`Không tìm thấy button có data-bs-theme-value="${theme}"`)
            return
        }

        const iconOfActiveBtn = btnToActive.querySelector('i')
        if (!iconOfActiveBtn) {
            console.warn("Không tìm thấy icon trong button")
            return
        }

        document.querySelectorAll('[data-bs-theme-value]').forEach(element => {
            element.classList.remove('active')
            element.setAttribute('aria-pressed', 'false')
        })

        btnToActive.classList.add('active')
        btnToActive.setAttribute('aria-pressed', 'true')

        // Đổi icon active
        activeThemeIcon.classList.replace(activeThemeIcon.classList[1], iconOfActiveBtn.classList[1])

        const themeSwitcherLabel = `${themeSwitcherText.textContent} (${btnToActive.dataset.bsThemeValue})`
        themeSwitcher.setAttribute('aria-label', themeSwitcherLabel)

        if (focus) {
            themeSwitcher.focus()
        }
    }

    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', () => {
        const storedTheme = getStoredTheme()
        if (storedTheme !== 'light' && storedTheme !== 'dark') {
            setTheme(getPreferredTheme())
        }
    })

    window.addEventListener('DOMContentLoaded', () => {
        showActiveTheme(getPreferredTheme())

        document.querySelectorAll('[data-bs-theme-value]')
            .forEach(toggle => {
                toggle.addEventListener('click', () => {
                    const theme = toggle.getAttribute('data-bs-theme-value')
                    setStoredTheme(theme)
                    setTheme(theme)
                    showActiveTheme(theme, true)
                })
            })
    })
})()
