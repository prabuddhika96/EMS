import React, {
  useEffect,
  useLayoutEffect,
  useState,
  type ReactNode,
} from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import type { RootState } from "../../redux/store";
import type { User } from "../../interface/User";
import { RouteName } from "../../constants/routeNames";
import "./style.css";
import Sidebar from "../../components/Sidebar/Sidebar";

interface Props {
  children: ReactNode;
  title?: string;
}
function MainLayout({ children, title }: Props) {
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
            <Sidebar isOpen={open} toggleDrawer={toggleDrawer} />
          </div>

          <div className="main-layout-children">
            <div className="main-layout-children-header">
              <h2 id="siteName">EMS</h2>
              <h2 className="logout-btn-container">Logout</h2>
            </div>
            {title && <h2 id="title">{title}</h2>}
            <div>{children}</div>
          </div>
        </div>
      )}
    </>
  );
}

export default MainLayout;
