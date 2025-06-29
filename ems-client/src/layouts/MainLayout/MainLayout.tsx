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

interface Props {
  children: ReactNode;
}
function MainLayout({ children }: Props) {
  const navigate = useNavigate();
  const loggedUser: User = useSelector((state: RootState) => state.user);
  const [isMounted, setIsMounted] = useState(false);

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
        <div>
          <p>Main Layout</p>
          {children}
        </div>
      )}
    </>
  );
}

export default MainLayout;
