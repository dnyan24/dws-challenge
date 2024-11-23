Added transferMoney Postmapping method in RestController AccountsController.
Added transferMoney in service layer, before going to production we can throw the exceptions instead of returning false , we can write it in better way.
Created TransferAmountFunctionalityTest junit test class with contains all the test cases for transferMoney function inside Service class
Used Maven as build and dependency management tool instaed of gradle.
