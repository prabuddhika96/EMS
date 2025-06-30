import React, {
  useEffect,
  useLayoutEffect,
  useState,
  type ReactNode,
} from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import type { RootState } from "../../redux/store";
import type { User } from "../../interface/User";
import { RouteName } from "../../constants/routeNames";
import "./style.css";
import Sidebar from "../../components/Sidebar/Sidebar";
import { authService } from "../../service/authService";
import { logoutUser } from "../../redux/slice/userSlice";
import ProtectedRoutes from "../../routes/ProtectedRoutes";

interface Props {
  children: ReactNode;
  title?: string;
  allowedRoles: ("ADMIN" | "USER")[];
}
function MainLayout({ children, title, allowedRoles }: Props) {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const loggedUser: User = useSelector((state: RootState) => state.user);
  const [isMounted, setIsMounted] = useState<boolean>(false);
  const [open, setOpen] = useState<boolean>(false);

  const toggleDrawer = (val: boolean) => {
    setOpen(val);
  };

  useEffect(() => {
    setIsMounted(true);
  }, []);

  useLayoutEffect(() => {
    if (loggedUser?.id == null) {
      navigate(RouteName.Home);
    }
  }, []);

  const handleLogout = async () => {
    try {
      const apiResponse: any = await authService.logout();

      if (apiResponse instanceof Error) {
        console.error("Failed to retrieve data:", apiResponse.message);
      } else {
        console.log(apiResponse?.data);
        if (apiResponse?.data?.code == 1002) {
          await dispatch(logoutUser());
          navigate(RouteName.Home);
        }
      }
    } catch (error) {}
  };

  if (!isMounted) return null;
  return (
    <>
      {isMounted && loggedUser?.id != null && (
        <div className="main-layout-container">
          <div
            className={`${
              open ? `main-layout-sidebar-open` : `main-layout-sidebar-close`
            } main-layout-sidebar`}
          >
            <Sidebar
              isOpen={open}
              toggleDrawer={toggleDrawer}
              loggedUser={loggedUser}
            />
          </div>

          <div className="main-layout-children">
            <div className="main-layout-children-header">
              <div className="main-layout-children-header-left">
                <h2 id="siteName">EMS</h2>
                {title && <h2 id="title">{title}</h2>}
              </div>
              <button className="logout-btn" onClick={handleLogout}>
                <svg
                  viewBox="0 0 20 20"
                  fill="currentColor"
                  aria-hidden="true"
                  style={{ display: "inline", verticalAlign: "middle" }}
                >
                  <path
                    fillRule="evenodd"
                    d="M7.75 3.5A.75.75 0 0 1 8.5 2.75h5A2.75 2.75 0 0 1 16.25 5.5v9A2.75 2.75 0 0 1 13.5 17.25h-5a.75.75 0 0 1 0-1.5h5A1.25 1.25 0 0 0 14.75 14.5v-9A1.25 1.25 0 0 0 13.5 4.25h-5a.75.75 0 0 1-.75-.75zM3.22 10.53a.75.75 0 0 1 0-1.06l2.72-2.72a.75.75 0 1 1 1.06 1.06l-1.19 1.19h5.44a.75.75 0 0 1 0 1.5H5.81l1.19 1.19a.75.75 0 1 1-1.06 1.06l-2.72-2.72z"
                    clipRule="evenodd"
                  />
                </svg>
                Logout
              </button>
            </div>

            <ProtectedRoutes
              allowedRoles={allowedRoles}
              loggedUserRole={loggedUser?.role ? loggedUser?.role : "USER"}
            >
              <div className="main-children-container">{children}</div>
            </ProtectedRoutes>
          </div>
        </div>
      )}
    </>
  );
}

export default MainLayout;
