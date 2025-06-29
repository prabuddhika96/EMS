import React, { type ReactNode } from "react";
import PaginationComponent from "../Pagination/PaginationComponent";
import "./style.css";

interface Props {
  children: ReactNode;
  totalPages?: number;
  page?: number;
  handlePageChange?: any;
}
function GridTemplate({ children, totalPages, page, handlePageChange }: Props) {
  return (
    <div>
      <div className="grid-container">
        <div className="dashboard-grid">{children}</div>
      </div>

      <div className="pagination-row">
        {totalPages && page && handlePageChange && (
          <PaginationComponent
            totalPages={totalPages}
            currentPage={page}
            onPageChnage={handlePageChange}
          />
        )}
        <p>pagination</p>
      </div>
    </div>
  );
}

export default GridTemplate;
