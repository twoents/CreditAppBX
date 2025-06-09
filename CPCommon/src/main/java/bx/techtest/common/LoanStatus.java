package bx.techtest.common;

import java.util.HashMap;

public enum LoanStatus {
    ACTIVE( "A" ),
    SETTLED( "S");
    
    private String statusValue;

    private LoanStatus(String statusValue) {
        this.statusValue = statusValue;
    }
    
    public String getStatusValue() {
        return( statusValue );
    }

    public static LoanStatus fromStatusValue( String s ) {
        for ( LoanStatus enumValue : LoanStatus.values() ) {
            if ( enumValue.getStatusValue().equals(s) ) {
                return( enumValue );
            }
        }
        throw new RuntimeException();
    }
}