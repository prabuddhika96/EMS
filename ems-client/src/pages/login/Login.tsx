import React, { useState } from "react";
import "./Login.css";
import type { LoginForm } from "../../interface/Form";
import { validateLoginForm } from "../../util/validation";
import { authService } from "../../service/authService";
import { useDispatch } from "react-redux";
import { setUser } from "../../redux/slice/userSlice";
import { useNavigate } from "react-router-dom";
import { RouteName } from "../../constants/routeNames";

const initialState: LoginForm = {
  email: "",
  password: "",
};

function Login() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [formData, setFormData] = useState<LoginForm>(initialState);
  const [errors, setErrors] = useState<LoginForm>(initialState);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrors(initialState);
    const hasError: boolean = validateLoginForm(formData, setErrors);

    if (hasError) {
      return;
    }

    try {
      const apiResponse: any = await authService.login(formData);
      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        console.log(apiResponse?.data);
        if (apiResponse?.data?.code == 1000) {
          await dispatch(setUser(apiResponse?.data?.data?.user));
          navigate(RouteName.Dashboard);
        }
      }
    } catch (error) {}
  };

  const handleChnage = (name: keyof LoginForm, value: string) => {
    setFormData((prev: LoginForm) => ({
      ...prev,
      [name]: value,
    }));
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div className="login-field">
          <label>Email</label>
          <input
            type="email"
            value={formData.email}
            onChange={(e) => handleChnage("email", e.target.value)}
          />
          <p className="error-text">{errors.email}</p>
        </div>
        <div className="login-field">
          <label>Password</label>
          <input
            type="password"
            value={formData.password}
            onChange={(e) => handleChnage("password", e.target.value)}
          />
          <p className="error-text">{errors.password}</p>
        </div>
        <button type="submit" className="login-button">
          Login
        </button>
      </form>
    </div>
  );
}

export default Login;
