package bx.techtest.common;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LoanStatusConverter implements AttributeConverter<LoanStatus, String> {

    @Override
    public String convertToDatabaseColumn(LoanStatus loanStatus) {
        return( loanStatus.getStatusValue() );
    }

    @Override
    public LoanStatus convertToEntityAttribute(String value) {
        return( LoanStatus.fromStatusValue(value ) );
    }
}