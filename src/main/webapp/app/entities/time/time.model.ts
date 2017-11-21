import { BaseEntity } from './../../shared';

export class Time implements BaseEntity {
    constructor(
        public id?: number,
        public route?: string,
        public bus_no?: string,
        public from?: string,
        public to?: string,
        public departure?: string,
        public arrival?: string,
        public frequency?: string,
    ) {
    }
}
