import React from "react";
import type { IOption, IOptionItem } from "../../interface/Option";

interface Props {
  options: IOption[];
  onChange: (value: string) => void;
  value?: string;
}

function PageSelector({ options, onChange, value }: Props) {
  const allItems: IOptionItem[] = options.flatMap((option) => option.items);

  return (
    <select
      className="page-selector"
      value={value}
      onChange={(e) => onChange(e.target.value)}
    >
      {allItems.map((item, index) => (
        <option key={index} value={item.value}>
          {Number(item.label).toString().padStart(2, "0")}
        </option>
      ))}
    </select>
  );
}

export default PageSelector;
