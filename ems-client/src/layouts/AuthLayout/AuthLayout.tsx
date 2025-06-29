import React, {
  useEffect,
  useLayoutEffect,
  useState,
  type ReactNode,
} from "react";
import "./style.css";
import bg_image from "../../assets/login.jpg";
import { useSelector } from "react-redux";
import type { User } from "../../interface/User";
import type { RootState } from "../../redux/store";
import { useNavigate } from "react-router-dom";
import { RouteName } from "../../constants/routeNames";

interface Props {
  children: ReactNode;
}

function AuthLayout({ children }: Props) {
  const navigate = useNavigate();
  const loggedUser: User = useSelector((state: RootState) => state.user);
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  useLayoutEffect(() => {
    if (loggedUser?.id != null) {
      navigate(RouteName.Dashboard);
    }
  }, []);

  if (!isMounted) return null;
  return (
    <>
      {isMounted && loggedUser?.id == null && (
        <div className="page-container">
          <div className="form-container">{children}</div>
          <div
            className="image-container"
            style={{ backgroundImage: `url(${bg_image})` }}
          />
        </div>
      )}
    </>
  );
}

export default AuthLayout;
