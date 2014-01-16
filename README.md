First run `CollocationNetworkBuilderWF` to create a collocation network resource and export it to `tmp/collocation-network.csv`. Then, run `MboxWF` to build lexical chains, link messages and export results to `tmp/results.digest`. 

Results can be evaluated with the following command: 
```
perl compare-sets-inTermsOf-PrecisionRecallFScore.pl -r data/in-reply-to-list.digest -c tmp/results.digest
```
