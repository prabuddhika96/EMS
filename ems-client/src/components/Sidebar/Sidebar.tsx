import React from "react";
import "./style.css";
import { useNavigate } from "react-router-dom";
import { RouteName } from "../../constants/routeNames";
import { NavData, type NavItem } from "../../constants/NavData";
import type { User } from "../../interface/User";

interface ISideBarProps {
  isOpen: boolean;
  toggleDrawer: (isOpen: boolean) => void;
  loggedUser: User;
}

function Sidebar({ isOpen, toggleDrawer, loggedUser }: ISideBarProps) {
  const navigate = useNavigate();

  const handleClickLogo = () => {
    navigate(RouteName.Dashboard);
  };

  return (
    <div
      className={`side_panel ${isOpen ? "open" : "closed"}`}
      onMouseEnter={() => toggleDrawer(true)}
      onMouseLeave={() => toggleDrawer(false)}
    >
      {NavData.map((item: NavItem, index: number) => (
        <>
          {item.hasAccess.includes(
            loggedUser?.role ? loggedUser?.role : "USER"
          ) && (
            <div
              key={index}
              className="side_item"
              onClick={() => navigate(item.routeName)}
            >
              <div
                className={`icon-container ${
                  isOpen ? "is-sidebar-open" : "is-sidebar-close"
                }`}
              >
                <item.icon size={30} />
              </div>
              <p
                className={`side_item_text ml-3 ${
                  isOpen ? `fade-in delay-${index}` : "fade-out"
                }`}
              >
                {item.name}
              </p>
            </div>
          )}
        </>
      ))}
    </div>
  );
}

export default Sidebar;
