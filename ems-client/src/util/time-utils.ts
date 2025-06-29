export const convertToCleanUTCISOString = (localDateTime: string): string => {
    const date = new Date(localDateTime);
    return date.toISOString().replace(/\.\d{3}Z$/, "Z");
};
