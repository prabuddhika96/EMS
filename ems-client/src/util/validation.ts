import type { LoginForm } from "../interface/Form";

export const validateLoginForm = (formData: LoginForm, setErrors: any): boolean => {
    let hasError: boolean = false

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email) {
        setErrors((prev: any) => ({ ...prev, email: "Email is required." }));
        hasError = true
    } else if (!emailRegex.test(formData.email)) {
        setErrors((prev: any) => ({ ...prev, email: "Invalid email format." }));
        hasError = true
    }

    // Password validation
    if (!formData.password) {
        setErrors((prev: any) => ({ ...prev, password: "Password is required." }));
        hasError = true
    } else if (formData.password.length < 6) {
        setErrors((prev: any) => ({ ...prev, password: "Password must be at least 6 characters." }));
        hasError = true
    }

    return hasError
}
