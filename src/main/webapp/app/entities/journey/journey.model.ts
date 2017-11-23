import { BaseEntity } from './../../shared';

export class Journey implements BaseEntity {
    constructor(
        public id?: number,
        public journeyDate?: string,
        public startingPoint?: string,
        public destination?: string,
        public departureTime?: string,
        public busNumber?: string,
        public payment?: BaseEntity,
        public passenger?: BaseEntity,
        public smartCard?: BaseEntity,
        public route?: BaseEntity,
    ) {
    }
}
