package Calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/*
"" ->0
"1"->1
"1,2"->3
"1\n2,3"->6
"//[;]\n1;2"->3
"1,-2"->exception with all negative numbers
"1,1001"->1
"//[***]\n1***2***3"->6
"//[*][%]\n1*2%3"->6
 */

class sumSould {

    public int add (String entry) throws NegativeException {

        if (entry.isEmpty()) {
            return 0;
        }

        boolean isAUniqueNumber = entry.length() == 1;
        if (isAUniqueNumber) {
            return Integer.parseInt(entry);
        }

        String newDelimiterCommand = "//";
        if (entry.contains (newDelimiterCommand)) {
            entry = setNewDelimiter(entry);
        }

        String newLine = "\n";
        if (entry.contains(newLine)) {
            entry = entry.replace(newLine, ",");
        }

        String[] numbers = entry.split(",");
        int sum = 0;
        StringBuilder negativeNumbers = new StringBuilder();
        for (String number :
                numbers) {
            if (Integer.parseInt(number) < 0)
                negativeNumbers.append(" ").append(number);
            else
                if ( Integer.parseInt(number)<1000)
                    sum += Integer.parseInt(number);
        }

        if (negativeNumbers.length() > 0) {
            throw new NegativeException("Error: Negatives numbers not allowed " + negativeNumbers);
        }

        return sum ;
    }


    private String setNewDelimiter(String entry) {

        String newLine = "\n";
        String[] entrySubdivisions = entry.split(newLine,0);
        String delimitersSubdivision = entrySubdivisions[0].substring(2);
        String numberSubdivision = entrySubdivisions[1];
        delimitersSubdivision = delimitersSubdivision.replace("[","");
        String [] delimiters = delimitersSubdivision.split("]");
        for (String delimiter : delimiters) {
            numberSubdivision = numberSubdivision.replace(delimiter, ",");
        }
        entry = numberSubdivision;
        return entry;
    }




    @Test
    void empty_entry_results_0() throws NegativeException {
        assertEquals(0, add(""));
    }

    @Test
    void a_number_results_the_same_number() throws NegativeException {
        assertEquals(1, add("1"));
    }

    @Test
    void two_numbers_results_the_sum_of_the_numbers() throws NegativeException {
        assertEquals(3, add("1,2"));
    }

    @Test
    void add_all_number_from_the_text() throws NegativeException {
        assertEquals(10, add("1,2,3,4"));
    }

    @Test
    void allow_new_line_between_numbers() throws NegativeException {
        assertEquals(6, add("1\n2,3"));
    }

    @Test
    void allow_support_different_delimiter() throws NegativeException {
        assertEquals(6, add("//[;]\n1;2;3"));
    }


   @Test
    void throw_exception_if_entry_contain_negatives_number() {
        assertThatExceptionOfType(NegativeException.class).isThrownBy(() -> add("1,-2,-3")).withMessage("Error: Negatives numbers not allowed  -2 -3");
    }
    @Test
    void ignore_numbers_greater_than_1000() throws NegativeException {
        assertEquals(1, add("1,1001"));
    }
    @Test
    void allow_delimiter_with_any_length() throws NegativeException {
        assertEquals(6, add("//[***]\n1***2***3"));
    }
    @Test
    void allow_multiple_delimiter() throws NegativeException {
        assertEquals(6, add("//[&][%]\n1&2%3"));
    }


}


