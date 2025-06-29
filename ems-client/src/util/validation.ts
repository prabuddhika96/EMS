import type { CreateEventForm, LoginForm } from "../interface/Form";

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


// export const validateCreateEventForm = (
//     form: CreateEventForm,
//     setErrors: any
// ): boolean => {
//     let hasError: boolean = false;

//     if (!form.title.trim()) {
//         setErrors((prev: any) => ({ ...prev, title: "Title is required." }));
//         hasError = true;
//     }

//     if (!form.description.trim()) {
//         setErrors((prev: any) => ({
//             ...prev,
//             description: "Description is required.",
//         }));
//         hasError = true;
//     }

//     if (!form.startTime) {
//         setErrors((prev: any) => ({
//             ...prev,
//             startTime: "Start time is required.",
//         }));
//         hasError = true;
//     }

//     if (!form.endTime) {
//         setErrors((prev: any) => ({
//             ...prev,
//             endTime: "End time is required.",
//         }));
//         hasError = true;
//     }

//     if (form.startTime && form.endTime) {
//         const now = new Date();
//         const start = new Date(form.startTime);
//         const end = new Date(form.endTime);

//         // Allow today: start and end must be >= now (not before now)
//         if (start < now) {
//             setErrors((prev: any) => ({
//                 ...prev,
//                 startTime: "Start time must be today or in the future.",
//             }));
//             hasError = true;
//         }

//         if (end < now) {
//             setErrors((prev: any) => ({
//                 ...prev,
//                 endTime: "End time must be today or in the future.",
//             }));
//             hasError = true;
//         }

//         if (start >= end) {
//             setErrors((prev: any) => ({
//                 ...prev,
//                 endTime: "End time must be after start time.",
//             }));
//             hasError = true;
//         }
//     }


//     if (!form.location.trim()) {
//         setErrors((prev: any) => ({
//             ...prev,
//             location: "Location is required.",
//         }));
//         hasError = true;
//     }

//     if (!form.visibility) {
//         setErrors((prev: any) => ({
//             ...prev,
//             visibility: "Visibility is required.",
//         }));
//         hasError = true;
//     }

//     return hasError;
// };

export const validateCreateEventForm = (
    form: CreateEventForm,
    setErrors: any
): boolean => {
    let hasError = false;
    const now = new Date();

    if (!form.title.trim()) {
        setErrors((prev: any) => ({ ...prev, title: "Title is required." }));
        hasError = true;
    }

    if (!form.description.trim()) {
        setErrors((prev: any) => ({
            ...prev,
            description: "Description is required.",
        }));
        hasError = true;
    }

    if (!form.startTime) {
        setErrors((prev: any) => ({
            ...prev,
            startTime: "Start time is required.",
        }));
        hasError = true;
    }

    if (!form.endTime) {
        setErrors((prev: any) => ({
            ...prev,
            endTime: "End time is required.",
        }));
        hasError = true;
    }

    if (form.startTime && form.endTime) {
        const start = new Date(form.startTime);
        const end = new Date(form.endTime);

        // Check startTime is >= now (allow now or future)
        if (start <= now) {
            setErrors((prev: any) => ({
                ...prev,
                startTime: "Start time must be now or in the future.",
            }));
            hasError = true;
        }

        // Check endTime is >= now (allow now or future)
        if (end < now) {
            setErrors((prev: any) => ({
                ...prev,
                endTime: "End time must be now or in the future.",
            }));
            hasError = true;
        }

        // Check endTime is strictly after startTime
        if (end <= start) {
            setErrors((prev: any) => ({
                ...prev,
                endTime: "End time must be after start time.",
            }));
            hasError = true;
        }
    }

    if (!form.location.trim()) {
        setErrors((prev: any) => ({
            ...prev,
            location: "Location is required.",
        }));
        hasError = true;
    }

    if (!form.visibility) {
        setErrors((prev: any) => ({
            ...prev,
            visibility: "Visibility is required.",
        }));
        hasError = true;
    }

    return hasError;
};
