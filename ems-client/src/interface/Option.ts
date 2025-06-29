export interface IOptionItem {
    label: string | number;
    value: string;
    data?: any;
}
export interface IOption {
    group: string;
    groupLabel?: string
    items: IOptionItem[]
}