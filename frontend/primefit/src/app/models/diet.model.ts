export class Diet {
    id!: number;
    name!: string;
    pdfUrl: string = "pdf";
    dietCategory!: DietCategory;
}

export enum DietCategory {
    LOW_CARB,
    VEGAN,
    GLUTEN_FREE,
    PALEO,
    MEDITERRANEAN
}