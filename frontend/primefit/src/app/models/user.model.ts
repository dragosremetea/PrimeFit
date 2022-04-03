import { Role } from "./role.model";

export interface User {
    id: number;
    username: string;
    password: string;
    firstName: string;
    lastName: string;
    height: number;
    weight: number;
    email: string;
    phoneNumber: string;
    dateOfBirth: Date;
    gymSubscriptionStartDate: Date;
    role: Role;
}