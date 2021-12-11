package com.university.semenenko.meterproject.Entity;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Payment {

    private Date paymentDate;
    private int monthOfCalculation;
    private Long personId;

    // тут реализован Expert pattern
    private Meter meter1;
    private Meter meter2;

    public Payment(final Date paymentDate, final int monthOfCalculation, final Long personId, final Meter meter1,
            final Meter meter2) {
        this.paymentDate = paymentDate;
        this.monthOfCalculation = monthOfCalculation;
        this.personId = personId;
        this.meter1 = meter1;
        this.meter2 = meter2;
    }

    public int getAmount() {
        return (meter1.getValueAfter() - meter1.getValueBefore()) * 5 + (meter2.getValueAfter()-meter2.getValueBefore()) * 10;
    }

}
