import React, { type ReactNode } from "react";
import PaginationComponent from "../Pagination/PaginationComponent";
import "./style.css";
import PageSizeComponent from "../PageSizeSelector/PageSizeComponent";

interface Props {
  children: ReactNode;
  totalRecords?: number;
  totalPages?: number;
  page?: number;
  handlePageChange?: any;
  handleChangePageSize?: any;
}
function GridTemplate({
  children,
  totalPages,
  page,
  handlePageChange,
  totalRecords,
  handleChangePageSize,
}: Props) {
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

        {totalRecords && handleChangePageSize && (
          <PageSizeComponent
            totalRecords={totalRecords}
            handleChangePageSize={handleChangePageSize}
          />
        )}
      </div>
    </div>
  );
}

export default GridTemplate;
